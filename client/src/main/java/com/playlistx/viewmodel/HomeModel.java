package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.music.Playlist;
import com.playlistx.view.ViewHandler;
import org.jetbrains.annotations.NotNull;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class HomeModel {
    private static HomeModel instance;
    private final Model model = Model.get();

    private HomeModel() throws RemoteException, NotBoundException {
    }

    public static @NotNull HomeModel get() throws NotBoundException, RemoteException {
        if (instance == null) return instance = new HomeModel();
        else return instance;
    }

    public @NotNull List<Playlist> getPlaylists() {
        try {
            return model.getAllPlaylists();
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }

    public
}