package C195RafaelSantos.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * <p>
 * Stores JDBC Connection Driver Parameters and basic connection methods
 *
 * @author Rafael Santos
 * </p>
 */
public class DatabaseConnection {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ06bRu";
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;
    private static final String MySQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static final String username = "U06bRu";
    private static final String password = "53688720179";

    private static Connection connection = null;

    /**
     * <p>
     * Initialized the connection to the DB. Will return an SQLException if there is an issue with the connection.
     * Connection parameters are defined as static Strings within this Class
     * </p>
     */
    public static void startConnection() {
        try {
            Class.forName(MySQLJDBCDriver);
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection was successful");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Return the current Connection
     * </p>
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * <p>
     * Closes the current Connection. Called when the program is closed.
     * </p>
     */
    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            //Do nothing (connection was already closed)
        }
    }
}
