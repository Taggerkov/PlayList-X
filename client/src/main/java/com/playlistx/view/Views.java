package com.playlistx.view;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public enum Views {
    LOGIN, HOME, PLAYLIST, SONG, USER, SETTINGS;

    public boolean show() {
        try {
            ViewHandler.get().display(this);
            return true;
        } catch (RemoteException | NotBoundException e) {
            return false;
        }
    }
}