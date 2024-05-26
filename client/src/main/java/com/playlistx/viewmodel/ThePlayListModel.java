package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.music.Playlist;
import com.playlistx.model.paths.CSS;
import com.playlistx.view.ViewHandler;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Logic Model for 'ThePlaylistView' view.
 * <br> This class is a {@code Singleton}.
 *
 * @author Sergiu Chirap
 * @version final
 * @since 0.7
 */
public class ThePlayListModel implements PropertyChangeListener {
    /**
     * This is the {@code Singleton} instance.
     */
    private static ThePlayListModel instance;
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
    private ThePlayListModel() throws RemoteException, NotBoundException {
        CSS.addListener(this);
        model.addListener(this);
    }

    /**
     * {@code Singleton} getter. Gets the singleton instance or creates a new one if none exists.
     *
     * @return The singleton instance.
     */
    public static @NotNull ThePlayListModel get() {
        try {
            if (instance == null) return instance = new ThePlayListModel();
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
        return instance;
    }

    /**
     * Gets the chosen {@link Playlist}.
     *
     * @param playListID ID of the chosen playlist.
     * @return The chosen playlist.
     */
    public Playlist getPlayList(int playListID) {
        try {
            return model.getPlaylist(playListID);
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Changes the scoped {@link Playlist} title with the provided parameters.
     *
     * @param playlistID ID of the scoped playlist.
     * @param newTitle The new title for the playlist.
     */
    public void newTitle(int playlistID, String newTitle) {
        try {
            model.getPlaylist(playlistID).setTitle(newTitle);
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
    }

    /**
     * Changes the scoped {@link Playlist} description with the provided parameters.
     * @param playlistID ID of the scoped playlist.
     * @param newDesc The new description for the playlist.
     */
    public void newDesc(int playlistID, String newDesc){
        // Feature not implemented - Sergiu C.
        ViewHandler.popUp(ViewHandler.Notify.ACCESS, "Feature Unavailable!");
    }

    /**
     * Changes scoped {@link Playlist} visibility.
     * @param playlistID ID of the scoped playlist.
     * @param temp Boolean provided by the controller.
     */
    public void isPublic(int playlistID, boolean temp) {
        try {
            model.getPlaylist(playlistID).setPublic(!temp);
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
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