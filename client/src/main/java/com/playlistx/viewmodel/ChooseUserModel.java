package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.music.Playlist;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChooseUserModel {
    private final Model model = Model.get();
    private static ChooseUserModel instance;
    private Playlist pl;

    private ChooseUserModel() throws RemoteException, NotBoundException {
    }

    public static ChooseUserModel get() throws RemoteException, NotBoundException {
        if (instance == null) instance = new ChooseUserModel();
        return instance;
    }

    public List<String> getUsers(int playlistID) {
        try {
            List<String> users = new ArrayList<>(List.of(model.getAllUsers()));
            pl = model.getPlaylist(playlistID);
            if (pl != null) {
                List<String> temp = pl.getCollaborators();
                temp.add(pl.getOwner());
                users.removeAll(temp);
            }
            return users;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(String collaborator) {
        pl.addCollaborator(collaborator);
    }
}