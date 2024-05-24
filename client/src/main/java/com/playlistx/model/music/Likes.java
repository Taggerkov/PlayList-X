package com.playlistx.model.music;

import com.playlistx.model.database.UserDAO;
import com.playlistx.model.database.SongDAO;

public class Likes {
    private int userId;
    private String songId;
    private UserDAO userDAO;
    private SongDAO songDAO;

    public Likes(int userId, String songId, UserDAO userDAO, SongDAO songDAO) {
        this.userId = userId;
        this.songId = songId;
        this.userDAO = userDAO;
        this.songDAO = songDAO;
    }

    public int getUserId() {
        return userDAO.getUserId(userId);
    }

    public int getSongId() {
        return songDAO.getSongId(songId);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}