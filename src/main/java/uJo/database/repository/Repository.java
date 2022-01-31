package uJo.database.repository;

import uJo.database.Database;
import uJo.database.query.*;
import uJo.decorator.tool.ExtractToClass;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Abraham Medina Carrillo <https://github.com/medina1402>
 */
public class Repository<T> {
    protected final Class<T> clazz;
    protected Connection connection = null;

    public Repository(Class<T> clazz) {
        this.clazz = clazz;
    }

    private ResultSet execResultSet(Query query) {
        if (connection == null) connection = Database.Connected();
        return Database.ExecuteQueryResultSet(query.toString(), connection);
    }

    private T singleInstance(ResultSet resultSet) {
        T newInstance = null;
        try {
            String[] columns = ExtractToClass.ColumnsNames(clazz);
            if (resultSet.next()) {
                newInstance = clazz.newInstance();
                setFieldNewInstance(newInstance, resultSet, columns);
            }
            resultSet.close();
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException ignore) {}

        return newInstance;
    }

    private ArrayList<T> multipleInstances(ResultSet resultSet) {
        ArrayList<T> newMultipleInstances = new ArrayList<>();
        try {
            String[] columns = ExtractToClass.ColumnsNames(clazz);
            while (resultSet.next()) {
                final T newInstance = clazz.newInstance();
                setFieldNewInstance(newInstance, resultSet, columns);
                newMultipleInstances.add(newInstance);
            }
            resultSet.close();
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException ignore) {}

        return newMultipleInstances;
    }

    private void setFieldNewInstance(T newInstance, ResultSet resultSet, String[] columns) throws NoSuchFieldException, SQLException, IllegalAccessException {
        for (int column = 0; column < columns.length; column++) {
            Field field = newInstance.getClass().getDeclaredField(columns[column]);
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            try {
                field.set(newInstance, resultSet.getObject(column + 1));
            } catch (Exception e) {
                field.set(newInstance, resultSet.getObject(column + 1).toString());
            }
            field.setAccessible(isAccessible);
        }
    }

    public boolean execute(Query query) {
        if (!Database.ExecuteQuery(query.toString())) {
            return Database.ExecuteQueryResultSet(query.toString(), connection) != null;
        }
        return true;
    }

    public boolean drop() {
        final Drop drop = new Drop();
        drop.table(ExtractToClass.TableName(clazz));
        return execute(drop);
    }

    public boolean update(Object id, String column, Object value) {
        return update(new String[]{column}, new Object[]{value}, new String[]{"id"},
                new Object[]{id}, new String[]{"="});
    }

    public boolean update(String field, Object newValue, String column, Object value, String compare) {
        return update(new String[]{field}, new Object[]{newValue}, new String[]{column},
                new Object[]{value}, new String[]{compare});
    }

    public boolean update(String[] fields, Object[] newValues, String[] columns, Object[] values, String[] compare) {
        final Update update = new Update();
        update.table(ExtractToClass.TableName(clazz));

        for(int k=0; k<fields.length; k++) update.set(fields[k], newValues[k]);
        for(int k=0; k<columns.length; k++) update.and(columns[k], values[k], compare[k]);

        return execute(update);
    }

    public boolean remove(Object id) {
        return remove(new String[]{"id"}, new Object[]{id}, new String[]{"="}, null);
    }

    public boolean remove(String column, Object value, String compare) {
        return remove(new String[]{column}, new Object[]{value}, new String[]{compare}, null);
    }

    public boolean remove(String[] columns, Object[] values, String[] compare, String[] concat) {
        final Remove remove = new Remove();
        remove.table(ExtractToClass.TableName(clazz));

        for(int k=0; k<columns.length; k++) {
            if (concat == null) remove.where(columns[k], values[k], compare[k]);
            else if (concat[k].equalsIgnoreCase("AND")) remove.and(columns[k], values[k], compare[k]);
            else if (concat[k].equalsIgnoreCase("OR")) remove.or(columns[k], values[k], compare[k]);
        }

        System.out.println(remove.toString());
        return execute(remove);
    }

    public T create(T item) {
        String[] columns = ExtractToClass.ColumnsNames(item.getClass());
        Insert insert = new Insert();
        insert.table(item.getClass());

        for (String column: columns) {
            try {
                Field field = item.getClass().getDeclaredField(column);
                final boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                insert.field(column, field.get(item));
                field.setAccessible(isAccessible);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String fieldPrimaryKey = null;
        for (LinkedHashMap<String, LinkedHashMap<String, String>> annotation: ExtractToClass.AnnotationsDeclared(item.getClass())) {
            String tempFieldPrimaryKey = "";
            for (LinkedHashMap<String, String> value: annotation.values()) {
                if (value.toString().split("\\{value=").length > 1 &&
                        !value.get("value").equalsIgnoreCase("~!@+")) tempFieldPrimaryKey = value.get("value");

                if (value.get("primaryKey") != null &&
                        value.get("primaryKey").equalsIgnoreCase("true")) fieldPrimaryKey = tempFieldPrimaryKey;
            }
        }

        if (Database.ExecuteQuery(insert.toString()) && fieldPrimaryKey != null) {
            return findLast(fieldPrimaryKey);
        }

        return null;
    }

    public T findOne(String key, Object value) {
        Select qsm = new Select();
        qsm.get().from(ExtractToClass.TableName(clazz)).where(key, value, "=").limit(1);

        ResultSet resultSet = execResultSet(qsm);
        if (resultSet == null) return null;
        return singleInstance(resultSet);
    }

    public T findOne(Object id) {
        return findOne("id", id);
    }

    public ArrayList<T> findAll() {
        return findAll(0);
    }

    public ArrayList<T> findAll(int limit) {
        Select qsm = new Select();
        qsm.get().from(ExtractToClass.TableName(clazz));
        if (limit > 0) qsm.limit(limit);

        ResultSet resultSet = execResultSet(qsm);
        if (resultSet == null) return new ArrayList<>();
        return multipleInstances(resultSet);
    }

    public T findFirst(String column) {
        Select qsm = new Select();
        qsm.get().from(ExtractToClass.TableName(clazz)).asc(column);

        ResultSet resultSet = execResultSet(qsm);
        if (resultSet == null) return null;
        return singleInstance(resultSet);
    }

    public T findLast(String column) {
        Select qsm = new Select();
        qsm.get().from(ExtractToClass.TableName(clazz)).desc(column);

        ResultSet resultSet = execResultSet(qsm);
        if (resultSet == null) return null;
        return singleInstance(resultSet);
    }

    public ArrayList<T> findWhere(String key, Object value) {
        return findWhere(key, value, 0);
    }

    public ArrayList<T> findWhere(String key, Object value, int limit) {
        return findWhere(new String[]{key}, new Object[]{value}, limit);
    }

    public ArrayList<T> findWhere(String[] keys, Object[] values) {
        return findWhere(keys, values, -1);
    }

    public ArrayList<T> findWhere(String[] keys, Object[] values, int limit) {
        Select qsm = new Select();
        qsm.get().from(ExtractToClass.TableName(clazz));
        if (limit > 0) qsm.limit(limit);

        for (int index=0; index < keys.length; index++) {
            qsm.and(keys[index], values[index], "=");
        }

        ResultSet resultSet = execResultSet(qsm);
        if (resultSet == null) return new ArrayList<>();
        return multipleInstances(resultSet);
    }

    public ArrayList<T> findLike(String column, String pattern) {
        return findLike(column, pattern, -1);
    }

    public ArrayList<T> findLike(String column, String pattern, int limit) {
        Select qsm = new Select();
        qsm.get().from(ExtractToClass.TableName(clazz)).like(column, pattern);
        if (limit > 0) qsm.limit(limit);

        ResultSet resultSet = execResultSet(qsm);
        if (resultSet == null) return new ArrayList<>();
        return multipleInstances(resultSet);
    }
}