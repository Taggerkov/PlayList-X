package com.playlistx.model.music;

import com.playlistx.model.database.ListenerDAO;

public class Listener {
    private int id;
    private String username;
    private boolean isAdmin;
    private byte[] hash;
    private byte[] salt;
    private ListenerDAO listenerDAO;

    public Listener(int id, String username, boolean isAdmin, byte[] hash, byte[] salt) {
        this.id = id;
        this.username = username;
        this.isAdmin = isAdmin;
        this.hash = hash;
        this.salt = salt;
        this.listenerDAO = listenerDAO;
    }

    public int getId() {
        return listenerDAO.getUserId(id);
    }

    public String getUsername() {
        return listenerDAO.getUserNameById(id);
    }

    public boolean isAdmin() {
        return listenerDAO.isUserAdmin(id);
    }

    public byte[] getHash() {
        return listenerDAO.getUserHash(id);
    }

    public byte[] getSalt() {
        return listenerDAO.getUserSalt(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}