package uJo.database.query;

/**
 *
 * @author Abraham Medina Carrillo <https://github.com/medina1402>
 */
public class Query {
    protected String[] manager;

    public String toString() {
        StringBuilder query = new StringBuilder();
        for (String str: manager) if (str != null) query.append(str);
        return query.toString().trim() + ";";
    }
}
