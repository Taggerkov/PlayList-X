package com.playlistx.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    private DatabaseConnector dbConnector;
    private int ownerid;

    public UserDAO() {
        this.dbConnector = new DatabaseConnector();
    }


    public int getUserId(int userId) {
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM listener WHERE id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // return -1 or any invalid value if user not found
    }

    public String getOwnerNameById(int ownerid) {
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT username FROM listener WHERE id = ?");
            statement.setInt(1, ownerid); // set ownerId as int
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // return null if user not found
    }
}