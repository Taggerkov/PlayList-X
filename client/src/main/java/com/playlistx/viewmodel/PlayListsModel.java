package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.login.User;
import com.playlistx.model.music.Playlist;
import com.playlistx.model.paths.CSS;
import com.playlistx.view.ViewHandler;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Logic Model for 'PlayLists' view.
 * <br> This class is a {@code Singleton}.
 *
 * @author Sergiu Chirap
 * @version final
 * @since 0.7
 */
public class PlayListsModel implements PropertyChangeListener {
    /**
     * This is the {@code Singleton} instance.
     */
    private static PlayListsModel instance;
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
    private PlayListsModel() throws RemoteException, NotBoundException {
        CSS.addListener(this);
        model.addListener(this);
    }

    /**
     * {@code Singleton} getter. Gets the singleton instance or creates a new one if none exists.
     *
     * @return The singleton instance.
     */
    public static @NotNull PlayListsModel get() {
        try {
            if (instance == null) return instance = new PlayListsModel();
            else return instance;
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets all available {@link Playlist} for the user from our database.
     * @return A {@link List<Playlist>} with all the available playlists for the user.
     */
    public @NotNull List<Playlist> getAllPlayLists() {
        try {
            return model.getAllPlaylists();
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }

    /**
     * Creates a new {@link Playlist}.
     */
    public void createNewPlayList() {
        Random random = new Random();
        try {
            model.createPlaylist(LocalDateTime.now().getNano() + random.nextInt(5000, 10000), User.get().getUsername() + "'s PlayList of " + LocalDateTime.now().truncatedTo(ChronoUnit.HOURS),
                    User.get().getUsername(), new ArrayList<>(), Date.valueOf(LocalDate.now()), 0, false);
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
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