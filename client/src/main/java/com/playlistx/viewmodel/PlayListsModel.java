package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.login.User;
import com.playlistx.model.music.Playlist;
import com.playlistx.model.paths.CSS;
import com.playlistx.view.PlayListsController;
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

public class PlayListsModel implements PropertyChangeListener {
    private static PlayListsModel instance;
    private final Model model = Model.get();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private PlayListsModel() throws RemoteException, NotBoundException {
        CSS.addListener(this);
        model.addListener(this);
    }

    public static @NotNull PlayListsModel get() {
        try {
            if (instance == null) return instance = new PlayListsModel();
            else return instance;
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            throw new RuntimeException(e);
        }
    }

    public @NotNull List<Playlist> getAllPlayLists() {
        try {
            return model.getAllPlaylists();
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }

    public void createNewPlayList() {
        Random random = new Random();
        try {
            model.createPlaylist(LocalDateTime.now().getNano() + random.nextInt(5000, 10000), User.get().getUsername() + "'s PlayList of " + LocalDateTime.now().truncatedTo(ChronoUnit.HOURS),
                    User.get().getOwnerId(), new ArrayList<>(), Date.valueOf(LocalDate.now()), 0, false); // pass ownerid as int
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
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
}