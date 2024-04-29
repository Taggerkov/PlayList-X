package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.music.Playlist;
import com.playlistx.view.ViewHandler;
import org.jetbrains.annotations.NotNull;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * HomeModel class handles the interaction between the view and the model layers,
 * specifically for operations related to fetching playlist information.
 */
public class HomeModel {
    private static HomeModel instance;
    private final Model model;

    /**
     * Private constructor to initialize the HomeModel with the Model layer.
     * Utilizes singleton pattern to ensure only one instance of this model exists.
     */
    private HomeModel() throws RemoteException, NotBoundException {
        model = Model.get();  // Retrieve the singleton instance of the model.
    }

    /**
     * Returns the singleton instance of HomeModel, creating it if necessary.
     * @return The single, static instance of the HomeModel.
     * @throws NotBoundException If there is an issue binding to the RMI registry.
     * @throws RemoteException If an RMI error occurs.
     */
    public static @NotNull HomeModel get() throws NotBoundException, RemoteException {
        if (instance == null) {
            instance = new HomeModel();
        }
        return instance;
    }

    /**
     * Retrieves a list of all playlists from the model.
     * @return A list of Playlist objects or an empty list if an error occurs.
     */
    public @NotNull List<Playlist> getPlaylists() {
        try {
            return model.getAllPlaylists();
        } catch (RemoteException e) {
            // Log and handle the error appropriately.
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a list of all playlist names from the model.
     * @return A list of strings containing playlist names or an empty list if an error occurs.
     */
    public @NotNull List<String> getPlaylistNames() {
        try {
            return model.getPlaylistNames();
        } catch (RemoteException e) {
            // Log and handle the error appropriately.
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }
}
