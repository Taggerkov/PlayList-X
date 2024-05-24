package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.music.Song;
import com.playlistx.model.paths.CSS;
import com.playlistx.view.ViewHandler;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SongListModel implements PropertyChangeListener {
    private static SongListModel instance;
    private final Model model = Model.get();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private SongListModel() throws RemoteException, NotBoundException {
        CSS.addListener(this);
        model.addListener(this);
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

    public void addListener(@NotNull PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(@NotNull PropertyChangeEvent evt) {
        pcs.firePropertyChange(evt);
    }
}