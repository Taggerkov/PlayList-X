package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.login.LoginException;
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

/**
 * Logic Model for 'Home' view.
 * <br> This class is a {@code Singleton}.
 *
 * @author Sergiu Chirap
 * @version final
 * @since 0.7
 */
public class HomeModel implements PropertyChangeListener {

    /**
     * This is the {@code Singleton} instance.
     */
    private static HomeModel instance;
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
    private HomeModel() throws RemoteException, NotBoundException {
        CSS.addListener(this);
        model.addListener(this);
    }

    /**
     * {@code Singleton} getter. Gets the singleton instance or creates a new one if none exists.
     *
     * @return The singleton instance.
     */
    public static @NotNull HomeModel get() {
        try {
            if (instance == null) return instance = new HomeModel();
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
        return instance;
    }

    /**
     * Gets all available {@link Playlist} for the user from our database.
     * @return A {@link List<Playlist>} with all the available playlists for the user.
     */
    public @NotNull List<Playlist> getPlaylistsAll() {
        try {
            return model.getAllPlaylists();
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }

    /**
     * Gets {@link Playlist Playlists} from {@link #getPlaylistsAll()} and sorts it by latest.
     * @return An ordered {@link List<Playlist>} of available playlists by latest.
     */
    public @NotNull List<Playlist> getPlaylistsLatest() {
        List<Playlist> playlists = getPlaylistsAll();
        if (playlists.size() <= 1) return playlists;
        playlists.sort(new PlayYearComparator());
        int index;
        if (playlists.size() > 15) index = 14;
        else index = playlists.size() - 1;
        return playlists.subList(0, index);
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
     * Gets {@link Song Songs} from {@link #getSongsAll()} and sorts it by latest.
     * @return An ordered {@link List<Song>} of available songs by latest.
     */
    public @NotNull List<Song> getSongsLatest() {
        List<Song> songs = getSongsAll();
        if (songs.size() <= 1) return songs;
        songs.sort(new SongYearComparator());
        int index;
        if (songs.size() > 15) index = 14;
        else index = songs.size() - 1;
        return songs.subList(0, index);
    }

    /**
     * Changes users username to the one provided.
     * @param newUsername A {@code String} stating the new username.
     * @param password A {@code String} stating the password.
     * @return A {@code boolean} which states if the operation was a success.
     */
    public boolean changeUsername(String newUsername, String password) {
        boolean awr = false;
        try {
            awr = User.get().changeUsername(newUsername, password);
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        } catch (LoginException e) {
            return false;
        }
        return awr;
    }

    /**
     * Changes users password to the one provided.
     * @param oldPassword A {@code String} stating the password.
     * @param newPassword A {@code String} stating the new password.
     * @return A {@code boolean} which states if the operation was a success.
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        boolean awr = false;
        try {
            awr = User.get().changePassword(oldPassword, newPassword);
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
        return awr;
    }

    /**
     * Gets all available {@link CSS}.
     * @return An array of all available CSS.
     */
    public CSS[] getCSS() {
        return CSS.values();
    }

    /**
     * Sets the provided {@link CSS} as active.
     * @param css The CSS to be active.
     */
    public void setCSS(@Nullable CSS css) {
        if (css != null) CSS.setCSS(css);
    }

    /**
     * Popup an {@link javafx.scene.control.Alert Alert}, if reconfirmed applications logs out and then closes.
     */
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

    /**
     * Adds a {@link PropertyChangeListener} to this class {@link PropertyChangeSupport}.
     * @param pcl The listener to be added to this class support.
     */
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
}