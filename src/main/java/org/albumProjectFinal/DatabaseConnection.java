package org.albumProjectFinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;

    public DatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/albumfinalinfoman";
            String user = "andreidaniel";
            String password = "1234";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully");
        } catch (SQLException ex) {
            System.err.println("Database connection failed: " + ex.getMessage());
        }
    }

    public Connection getConnection() {

        return connection;
    }
}
