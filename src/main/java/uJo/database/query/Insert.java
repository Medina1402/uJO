package uJo.database.query;

import uJo.decorator.tool.ExtractToClass;

/**
 *
 * @author Abraham Medina Carrillo <https://github.com/medina1402>
 * @see uJo.database.query.Query
 */
public class Insert extends Query {
    public Insert() {
        manager = new String[5];
        manager[0] = "INSERT INTO ";
        manager[2] = "";
        manager[3] = "";
    }

    public Insert table(String table) {
        manager[1] = table;
        return this;
    }

    public Insert table(Class<?> clazz) {
        manager[1] = ExtractToClass.TableName(clazz);
        return this;
    }

    public Insert field(String column, Object value) {
        if (value == null) return this;

        if (value.getClass().getSimpleName().equalsIgnoreCase("String")) value = "'" + value + "'";

        if (manager[2] == null || manager[2].length() < 1) {
            manager[2] = column;
            manager[3] = value.toString();
        } else {
            manager[2] += ", " + column;
            manager[3] += ", " + value;
        }
        return this;
    }

    @Override
    public String toString() {
        String query = manager[0] + manager[1];
        query += " (" + manager[2] + ")";
        query += " VALUES (" + manager[3] + ");";
        return query.trim();
    }
}
