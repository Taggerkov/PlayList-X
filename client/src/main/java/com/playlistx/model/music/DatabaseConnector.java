package com.playlistx.model.music;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private String url = "jdbc:mysql://localhost:3306/yourDatabase";
    private String username = "username";
    private String password = "password";

    public DatabaseConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // ensure the JDBC driver is loaded
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}