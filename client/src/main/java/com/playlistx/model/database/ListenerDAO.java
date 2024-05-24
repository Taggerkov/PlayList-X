package com.playlistx.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ListenerDAO {
    private DatabaseConnector dbConnector;

    public ListenerDAO() {
        this.dbConnector = new DatabaseConnector();
    }

    public String getListenerNameById(int listenerId) {
        String listenerName = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT username FROM Listener WHERE id = ?");
            statement.setInt(1, listenerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                listenerName = resultSet.getString("username");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listenerName;
    }

    public int getUserId(int userId) {
        return userId; // This method is redundant as it returns the same value that is passed to it.
    }

    public String getUserNameById(int userId) {
        return getListenerNameById(userId);
    }

    public boolean isUserAdmin(int userId) {
        boolean isAdmin = false;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT isAdmin FROM Listener WHERE id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isAdmin = resultSet.getBoolean("isAdmin");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAdmin;
    }

    public byte[] getUserHash(int userId) {
        byte[] hash = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT hash FROM Listener WHERE id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                hash = resultSet.getBytes("hash");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    public byte[] getUserSalt(int userId) {
        byte[] salt = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT salt FROM Listener WHERE id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                salt = resultSet.getBytes("salt");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salt;
    }
}