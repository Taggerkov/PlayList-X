package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.music.Playlist;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Logic Model for 'Choose User' view.
 * <br> This class is a {@code Singleton}.
 *
 * @author Sergiu Chirap
 * @version final
 * @since 0.7
 */
public class ChooseUserModel {
    /**
     * Client main model manager.
     */
    private final Model model = Model.get();
    /**
     * This is the {@code Singleton} instance.
     */
    private static ChooseUserModel instance;
    /**
     * Scoped {@link Playlist}.
     */
    private Playlist pl;

    /**
     * Class private constructor. Intended to run only once due to being a {@code Singleton}.
     *
     * @throws RemoteException   RMI connection error.
     * @throws NotBoundException RMI connection error.
     */
    private ChooseUserModel() throws RemoteException, NotBoundException {
    }

    /**
     * {@code Singleton} getter. Gets the singleton instance or creates a new one if none exists.
     *
     * @return The singleton instance.
     */
    public static ChooseUserModel get() throws RemoteException, NotBoundException {
        if (instance == null) instance = new ChooseUserModel();
        return instance;
    }

    /**
     * Gets all the users from our server and check for the ones available to share with.
     * @param playlistID The ID of the scoped {@link Playlist}.
     * @return A {@link List<String>} of all the users available for sharing.
     */
    public List<String> getUsers(int playlistID) {
        try {
            List<String> users = new ArrayList<>(List.of(model.getAllUsers()));
            pl = model.getPlaylist(playlistID);
            List<String> temp = pl.getCollaborators();
            temp.add(pl.getOwner());
            users.removeAll(temp);
            return users;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds chosen user as a collaborator to the scoped {@link Playlist}.
     * @param collaborator A {@code String} stating the username of the user to be collaborator.
     */
    public void addUser(String collaborator) {
        pl.addCollaborator(collaborator);
    }
}