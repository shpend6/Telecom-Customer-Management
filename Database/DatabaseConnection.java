package Database;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/OOP_Project";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
