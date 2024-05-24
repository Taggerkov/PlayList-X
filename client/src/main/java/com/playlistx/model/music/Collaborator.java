package com.playlistx.model.music;

import com.playlistx.model.database.ListenerDAO;
import com.playlistx.model.database.PlaylistDAO;

public class Collaborator {
    private int listenerId;
    private int playlistId;
    private ListenerDAO listenerDAO;
    private PlaylistDAO playlistDAO;

    public Collaborator(int listenerId, int playlistId, ListenerDAO listenerDAO, PlaylistDAO playlistDAO) {
        this.listenerId = listenerId;
        this.playlistId = playlistId;
        this.listenerDAO = listenerDAO;
        this.playlistDAO = playlistDAO;
    }

    public String getListenerName() {
        return listenerDAO.getListenerNameById(listenerId);
    }

    public String getPlaylistName() {
        return playlistDAO.getPlaylistNameById(playlistId);
    }

    public void setListenerId(int listenerId) {
        this.listenerId = listenerId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }
}