package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.music.Song;
import com.playlistx.view.ViewHandler;
import org.jetbrains.annotations.NotNull;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SongListModel {
    private static SongListModel instance;
    private final Model model = Model.get();

    private SongListModel() throws RemoteException, NotBoundException {
    }

    public static @NotNull SongListModel get() {
        try {
            if (instance == null) return instance = new SongListModel();
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
        return instance;
    }

    public @NotNull List<Song> getSongsAll() {
        try {
            return model.getAllSongs();
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }

    public void addToPlaylist(int selectPlaylistID, @NotNull Song song) {
        try {
            model.addSongToPlaylist(selectPlaylistID, song);
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
    }

    public void addSong() {
        ViewHandler.popUp(ViewHandler.Notify.ACCESS, "Feature still in Work! Contact us through email.");
    }
}