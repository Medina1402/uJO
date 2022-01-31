package uJo.database.query;

/**
 *
 * @author Abraham Medina Carrillo <https://github.com/medina1402>
 * @see uJo.database.query.Query
 */
public class Remove extends Query {
    public Remove() {
        manager = new String[3];
        manager[0] = "DELETE FROM ";
    }

    public Remove table(String table) {
        manager[1] = table + " WHERE ";
        return this;
    }

    public Remove where(String column, Object value, String compare) {
        return where(column, value, compare, null);
    }

    public Remove where(String column, Object value, String compare, String concat) {
        Object newValue = !value.getClass().getSimpleName().equalsIgnoreCase("String") ?value :"'" + value + "'";
        if (manager[2] == null || manager[2].length() < 1) {
            manager[2] = column + compare + newValue;
        } else if(concat != null) {
            manager[2] = manager[2] + " " + concat + " " + column + compare + newValue;
        }
        return this;
    }

    public Remove and(String column, Object value, String compare) {
        return where(column, value, compare, "AND");
    }

    public Remove or(String column, Object value, String compare) {
        return where(column, value, compare, "OR");
    }
}
