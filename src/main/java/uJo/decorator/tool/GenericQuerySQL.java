package uJo.decorator.tool;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Abraham Medina Carrillo <https://github.com/medina1402>
 */
public class GenericQuerySQL {
    /**
     *
     * @param clazz
     * @return
     */
    public static String SQLDropTable(Class<?> clazz) {
        return "DROP TABLE " + ExtractToClass.TableName(clazz);
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static String SQLCreateTable(Class<?> clazz) {
        LinkedHashMap<String, LinkedHashMap<String, String>> fields = ExtractToClass.FieldsDeclared(clazz);
        ArrayList<String> foreignKeys = new ArrayList<>();
        String queryCreateTable = "CREATE TABLE " + ExtractToClass.TableName(clazz) + "(";

        for (String key : fields.keySet()) {
            LinkedHashMap<String, String> field = fields.get(key);
            String queryField = key + " ";

            // TYPE -> <field> varchar
            queryField += field.get("type").toUpperCase().replace("TYPE", "");

            // SIZE -> <field> varchar(255)
            if (field.get("size") != null) {
                queryField += "(" + field.get("size") + ")";
            }

            // DEFAULT -> <field> varchar(255) default <value>
            if (!field.get("value").equalsIgnoreCase("~!@+")) {
                if (field.get("valueDefault") == null || field.get("valueDefault").equals("true")) {
                    queryField += " DEFAULT " + field.get("value");
                }
            }

            // NOT NULL
            if (field.get("notNull").equals("true")) {
                queryField += " NOT NULL";
            }

            // UNIQUE
            if (field.get("unique").equals("true")) {
                queryField += " UNIQUE";
            }

            // PRIMARY_KEY
            if (field.get("primaryKey").equals("true")) {
                queryField += " PRIMARY KEY";
            }

            // AUTOINCREMENT
            if (field.get("autoincrement") != null && field.get("autoincrement").equals("true")) {
                queryField += " AUTO_INCREMENT";
            }

            // FOREIGN_KEY
            if (!field.get("foreignKey").equals("void")) {
                final String fk = "FOREIGN KEY (" + key + ") REFERENCES " +
                        field.get("foreignKey").replace("class ", "") + "(" + key + ")";
                foreignKeys.add(fk);
            }

            queryCreateTable += "\n\t" + queryField + ",";
        }

        // Foreign Keys
        for (String fk: foreignKeys) {
            queryCreateTable += "\n\t" + fk + ",";
        }

        queryCreateTable = queryCreateTable.substring(0, queryCreateTable.length() - 1);
        queryCreateTable += "\n);";
        return queryCreateTable;
    }
}
