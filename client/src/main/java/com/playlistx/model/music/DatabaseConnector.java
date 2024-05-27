package com.playlistx.model.music;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private String url = "jdbc:postgresql://localhost:5432/yourDatabase"; // change to PostgreSQL JDBC URL
    private String username = "postgres";
    private String password = "vis";

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