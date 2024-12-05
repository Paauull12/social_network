package org.example.projfinal.repository.databaserepo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DatabaseConnection {
    INSTANCE;

    private Connection connection;
    private final String URL = "jdbc:postgresql://localhost:5432/socialnetwork";
    private final String USER = "postgres";
    private final String PASSWORD = "postgres";

    DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing the database connection", e);
            }
        }
    }
}