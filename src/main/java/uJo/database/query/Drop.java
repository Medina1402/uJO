package uJo.database.query;

/**
 *
 * @author Abraham Medina Carrillo <https://github.com/medina1402>
 * @see uJo.database.query.Query
 */
public class Drop extends Query {
    public Drop() {
        manager = new String[3];
        manager[0] = "DROP ";
    }

    public Drop table(String table) {
        manager[1] = "TABLE ";
        manager[2] = table;
        return this;
    }

    public Drop database(String database) {
        manager[1] = "DATABASE ";
        manager[2] = database;
        return this;
    }
}
