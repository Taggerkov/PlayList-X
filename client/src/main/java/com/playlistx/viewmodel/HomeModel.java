package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.login.User;
import com.playlistx.model.music.Playlist;
import com.playlistx.model.music.Song;
import com.playlistx.model.paths.CSS;
import com.playlistx.view.ViewHandler;
import com.playlistx.viewmodel.comparators.PlayYearComparator;
import com.playlistx.viewmodel.comparators.SongYearComparator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class HomeModel implements PropertyChangeListener {
    private static HomeModel instance;
    private final Model model = Model.get();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private HomeModel() throws RemoteException, NotBoundException {
        CSS.addListener(this);
        model.addListener(this);
    }

    public static @NotNull HomeModel get() {
        try {
            if (instance == null) return instance = new HomeModel();
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
        return instance;
    }

    public @NotNull List<Playlist> getPlaylistsAll() {
        try {
            return model.getAllPlaylists();
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }

    public @NotNull List<Playlist> getPlaylistsLatest() {
        List<Playlist> playlists = getPlaylistsAll();
        if (playlists.size() <= 1) return playlists;
        playlists.sort(new PlayYearComparator());
        int index;
        if (playlists.size() > 15) index = 14;
        else index = playlists.size() - 1;
        return playlists.subList(0, index);
    }

    public @NotNull List<Song> getSongsAll() {
        try {
            return model.getAllSongs();
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }

    public @NotNull List<Song> getSongsLatest() {
        List<Song> songs = getSongsAll();
        if (songs.size() <= 1) return songs;
        songs.sort(new SongYearComparator());
        int index;
        if (songs.size() > 15) index = 14;
        else index = songs.size() - 1;
        return songs.subList(0, index);
    }

    public boolean changeUsername(String newUsername, String password) {
        boolean awr = false;
        try {
            awr = User.get().changeUsername(newUsername, password);
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
        return awr;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        boolean awr = false;
        try {
            awr = User.get().changePassword(oldPassword, newPassword);
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
        return awr;
    }

    public CSS[] getCSS() {
        return CSS.values();
    }

    public void setCSS(@Nullable CSS css) {
        if (css != null) CSS.setCSS(css);
    }

    public void addListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        pcs.firePropertyChange(evt);
    }

    public void close() {
        if (ViewHandler.popUp(ViewHandler.Notify.CONFIRM, "Are you sure?")) {
            try {
                User.get().logout();
                System.exit(0);
            } catch (RemoteException | NotBoundException e) {
                ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            }
        }
    }
}