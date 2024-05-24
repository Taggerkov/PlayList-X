package com.playlistx.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=playlistx"; // the database URL
    private String username = "postgres";
    private String password = "10123432";

    public DatabaseConnector() {
        try {
            Class.forName("org.postgresql.Driver"); // ensure the PostgreSQL JDBC driver is loaded
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}