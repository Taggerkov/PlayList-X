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

public class ThePlayListModel implements PropertyChangeListener {
    private static ThePlayListModel instance;
    private final Model model = Model.get();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private ThePlayListModel() throws RemoteException, NotBoundException {
        CSS.addListener(this);
        model.addListener(this);
    }

    public static @NotNull ThePlayListModel get() {
        try {
            if (instance == null) return instance = new ThePlayListModel();
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
        return instance;
    }

    public Playlist getPlayList(int playListID) {
        try {
            return model.getPlaylist(playListID);
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            throw new RuntimeException(e);
        }
    }

    public void newTitle(int playlistID, String newTitle) throws NullPointerException {
        try {
            Playlist thePlayList = model.getPlaylist(playlistID);
            if (thePlayList != null) {
                thePlayList.setTitle(newTitle);
            } else {
                throw new NullPointerException("Playlist with ID " + playlistID + " not found");
            }
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
    }

    public void newDesc(int playlistID, String newDesc){
        ViewHandler.popUp(ViewHandler.Notify.ACCESS, "Feature Unavailable!");
    }

    public void isPublic(int playlistID, boolean temp) {
        try {
            model.getPlaylist(playlistID).setPublic(!temp);
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
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