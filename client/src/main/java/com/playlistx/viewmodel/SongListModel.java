package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.music.Song;
import com.playlistx.model.paths.CSS;
import com.playlistx.view.ViewHandler;
import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Logic Model for 'SongList' view.
 * <br> This class is a {@code Singleton}.
 *
 * @author Sergiu Chirap
 * @version final
 * @since 0.7
 */
public class SongListModel implements PropertyChangeListener {
    /**
     * This is the {@code Singleton} instance.
     */
    private static SongListModel instance;
    /**
     * Client main model manager.
     */
    private final Model model = Model.get();
    /**
     * Observer Pattern trigger manager.
     */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Class private constructor. Intended to run only once due to being a {@code Singleton}.
     *
     * @throws RemoteException   RMI connection error.
     * @throws NotBoundException RMI connection error.
     */
    private SongListModel() throws RemoteException, NotBoundException {
        CSS.addListener(this);
        model.addListener(this);
    }

    /**
     * {@code Singleton} getter. Gets the singleton instance or creates a new one if none exists.
     *
     * @return The singleton instance.
     */
    public static @NotNull SongListModel get() {
        try {
            if (instance == null) return instance = new SongListModel();
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
        return instance;
    }

    /**
     * Gets all the available {@link Song Songs} from our database.
     * @return A {@link List<Song>} of all the available song.
     */
    public @NotNull List<Song> getSongsAll() {
        try {
            return model.getAllSongs();
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }

    /**
     * Adds a desired {@link Song} to the scoped {@link com.playlistx.model.music.Playlist Playlist}.
     * @param selectPlaylistID ID of the scoped playlist.
     * @param song The song to be added.
     */
    public void addToPlaylist(int selectPlaylistID, @NotNull Song song) {
        try {
            model.addSongToPlaylist(selectPlaylistID, song);
            Notifications notificationTest=Notifications.create();
            notificationTest.position(Pos.BASELINE_RIGHT);
            notificationTest.title("PlayList X");
            notificationTest.text(song.getTitle() + " has been added to playlist!");
            notificationTest.show();
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
    }

    /**
     * Suggests the desired song to our admins.
     */
    public void addSong() {
        // Feature not implemented - Sergiu C.
        ViewHandler.popUp(ViewHandler.Notify.ACCESS, "Feature still in Work! Contact us through email.");
    }

    /**
     * Adds a {@link PropertyChangeListener} to this class {@link PropertyChangeSupport}.
     * @param pcl The listener to be added to this class support.
     */
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