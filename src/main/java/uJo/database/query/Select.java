package uJo.database.query;

/**
 *
 * @author Abraham Medina Carrillo <https://github.com/medina1402>
 * @see uJo.database.query.Query
 */
public class Select extends Query {
    public Select() {
        manager = new String[8];
        manager[0] = "SELECT ";
    }

    private Select where(String column, Object value, String compare, String concat) {
        Object newValue = !value.getClass().getSimpleName().equalsIgnoreCase("String") ?value :"'" + value + "'";
        if (manager[3] == null || manager[3].length() < 1) manager[3] = " WHERE " + column + compare + newValue;
        else manager[3] = manager[3] + " " + concat + " " + column + compare + newValue;
        return this;
    }

    private Select order(String column, String order) {
        manager[6] = " ORDER BY " + column + " " + order;
        return this;
    }

    public Select get(String column) {
        if (manager[1] == null || manager[3].length() < 1) manager[1] = column;
        else manager[1] += ", " + column;
        return this;
    }

    public Select get() {
        return get("*");
    }

    public Select from(String table) {
        manager[2] = " FROM " + table;
        return this;
    }

    public Select from(String table, String key) {
        manager[2] = " FROM " + table + " AS " + key;
        return this;
    }

    public Select where(String column, Object value, String compare) {
        manager[3] = null;
        manager[4] = null;
        return where(column, value, compare, null);
    }

    public Select and(String column, Object value, String compare) {
        return where(column, value, compare, "AND");
    }

    public Select or(String column, Object value, String compare) {
        return where(column, value, compare, "OR");
    }

    public Select asc(String column) {
        return order(column, "ASC");
    }

    public Select desc(String column) {
        return order(column, "DESC");
    }

    public Select group(String column) {
        manager[5] = " GROUP BY " + column;
        return this;
    }

    public Select like(String column, String pattern) {
        manager[3] = " WHERE " + column;
        manager[4] = " LIKE '" + pattern + "'";
        return this;
    }

    public Select limit(int size) {
        manager[7] = " LIMIT " + size;
        return this;
    }
}
