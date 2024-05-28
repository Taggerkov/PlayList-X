package com.playlistx.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The DatabaseConnector class provides methods to establish a connection to a PostgreSQL database.
 */
public class DatabaseConnector {
    private String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=playlistx"; // the database URL
    private String username = "postgres"; // the username for the database connection
    private String password = "10123432"; // the password for the database connection

    /**
     * Constructs a DatabaseConnector object.
     * This constructor ensures that the PostgreSQL JDBC driver is loaded.
     */
    public DatabaseConnector() {
        try {
            Class.forName("org.postgresql.Driver"); // ensure the PostgreSQL JDBC driver is loaded
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // print stack trace if the driver class is not found
        }
    }

    /**
     * Establishes a connection to the PostgreSQL database.
     *
     * @return a Connection object representing the database connection.
     * @throws SQLException if a database access error occurs or the URL is null.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password); // establish a connection using the URL, username, and password
    }
}
