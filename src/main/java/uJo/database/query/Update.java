package uJo.database.query;

/**
 *
 * @author Abraham Medina Carrillo <https://github.com/medina1402>
 * @see uJo.database.query.Query
 */
public class Update extends Query {
    public Update() {
        manager = new String[4];
        manager[0] = "UPDATE ";
    }

    public Update table(String table) {
        manager[1] = table + " ";
        return this;
    }

    public Update set(String column, Object value) {
        Object newValue = !value.getClass().getSimpleName().equalsIgnoreCase("String") ?value :"'" + value + "'";
        if (manager[2] == null) manager[2] = "SET " + column + "=" + newValue;
        else manager[2] = manager[2] + ", " + column + "=" + newValue;
        return this;
    }

    public Update where(String column, Object value, String compare) {
        return where(column, value, compare, null);
    }

    public Update where(String column, Object value, String compare, String concat) {
        Object newValue = !value.getClass().getSimpleName().equalsIgnoreCase("String") ?value :"'" + value + "'";
        if (manager[3] == null || manager[3].length() < 1) {
            manager[3] = " WHERE " + column + compare + newValue;
        } else if(concat != null) {
            manager[3] = manager[3] + " " + concat + " " + column + compare + newValue;
        }
        return this;
    }

    public Update and(String column, Object value, String compare) {
        return where(column, value, compare, "AND");
    }

    public Update or(String column, Object value, String compare) {
        return where(column, value, compare, "OR");
    }
}
