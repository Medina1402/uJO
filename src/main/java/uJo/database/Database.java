package uJo.database;

import uJo.database.query.Drop;
import uJo.decorator.tool.GenericQuerySQL;

import java.sql.*;
import java.util.Objects;

/**
 *
 * @author Abraham Medina Carrillo <https://github.com/medina1402>
 */
public class Database {
    public static String HOST = "localhost";
    public static String DATABASE = "uJO";
    public static String USER = "root";
    public static String PASSWORD = "";
    private static String ADAPTER = "jdbc:mysql://";

    /**
     *
     * @return
     */
    public static Connection Connected() {
        try {
            return DriverManager.getConnection(ADAPTER + HOST + "/" + DATABASE, USER, PASSWORD);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     *
     * @param query
     * @return
     */
    public static boolean ExecuteQuery(String query) {
        boolean status = false;
        try {
            final Connection connection = Connected();
            if (connection == null) return false;

            Statement statement = connection.createStatement();
            status = statement.executeUpdate(query) == 1;

            statement.close();
            connection.close();
        } catch (SQLException ignore) {}

        return status;
    }

    /**
     *
     * @param query
     * @return
     */
    public static ResultSet ExecuteQueryResultSet(String query, Connection connection) {
        try {
            if (connection == null) return null;
            PreparedStatement prepareStatement = connection.prepareStatement(query);
            return prepareStatement.executeQuery();

        } catch (SQLException e) {
            return null;
        }
    }

    /**
     *
     * @param clazz
     */
    public static void CreateTable(Class<?> clazz) {
        try {
            Connection connection = Database.Connected();
            Database.ExecuteQuery(GenericQuerySQL.SQLCreateTable(clazz));
            Objects.requireNonNull(connection).close();
        } catch (SQLException ignore) {}
    }

    /**
     *
     * @param clazz
     */
    public static void DropTable(Class<?> clazz) {
        try {
            Connection connection = Database.Connected();
            Database.ExecuteQuery(GenericQuerySQL.SQLDropTable(clazz));
            Objects.requireNonNull(connection).close();
        } catch (SQLException ignore) {}
    }

    /**
     *
     * @param database
     */
    public static void DropDatabase(String database) {
        try {
            Connection connection = Database.Connected();
            final Drop drop = new Drop();
            drop.database(database);
            Database.ExecuteQuery(drop.toString());
            Objects.requireNonNull(connection).close();
        } catch (SQLException ignore) {}
    }
}
