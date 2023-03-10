package seals.c195task.Helper;

import seals.c195task.Controllers.EstablishConnectionStatus;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * CONNECT TO DATABASE
 */
public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";

    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String username = "sqlUser";
    private static final String password = "Passw0rd!";
    public static Connection connection;

    /**
     * LAMBDA EXPRESSION #2 ESTABLISH CONNECTION STATUS BY SHOWING IN CONSOLE
     * OPEN CONNECTION AND PRINT TO SCREEN
     */
    public static void openConnection() {
        try{
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            EstablishConnectionStatus printConnection = () -> {
                System.out.println("Connection Successful");
            };
            printConnection.connection();

        } catch(Exception e) {
            EstablishConnectionStatus printConnection = () -> {
                System.out.println("Error:" + e.getMessage());
            };
            printConnection.connection();
        }
    }

    /**
     * CLOSE CONNECTION AND PRINT TO SCREEN
     */
    public static void closeConnection() {
        try{
            connection.close();
            System.out.println("Connection Closed");
        } catch(Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }


}
