package com.playlistx.model;

import com.playlistx.model.login.LoginException;
import com.playlistx.model.proxy.Client;
import com.playlistx.model.proxy.SongService;
import com.playlistx.model.proxy.RemoteListener;
import com.playlistx.model.proxy.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import com.playlistx.model.music.Playlist;
import com.playlistx.model.music.Song;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * The ModelManager class implements the Model interface and handles client logic,
 * user authentication, and playlist management. It interacts with remote services
 * using RMI and notifies listeners about property changes.
 */
public class ModelManager implements Model, PropertyChangeListener {
    private static ModelManager instance;
    private final Session session;
    private final SongService songService;
    private final RemoteListener remoteListener;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final Map<Integer, Playlist> playlists = new HashMap<>();
    private final Client client;

    /**
     * Private constructor to initialize the ModelManager.
     * Sets up RMI connections to the Session and SongService.
     * Loads pre-created playlists.
     *
     * @throws RemoteException   if there is an RMI connection error.
     * @throws NotBoundException if the RMI binding is not found.
     */
    private ModelManager() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1099);
        session = (Session) registry.lookup("session");
        songService = (SongService) registry.lookup("SongService");
        remoteListener = new RemoteListener(this, session);
        client = (Client) registry.lookup(session.getClient());
        loadPreCreatedPlaylists(); // Load the pre-created playlists from the SongService
    }

    /**
     * Gets the singleton instance of ModelManager.
     *
     * @return the instance of ModelManager.
     * @throws RemoteException   if there is an RMI connection error.
     * @throws NotBoundException if the RMI binding is not found.
     */
    public static Model get() throws RemoteException, NotBoundException {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }

    /**
     * Logs the user in.
     *
     * @param username the username.
     * @param password the password.
     * @return a string indicating the login status.
     * @throws RemoteException if there is an RMI connection error.
     * @throws LoginException  if there is an error during login.
     */
    @Override
    public String login(String username, byte[] password) throws RemoteException, LoginException {
        return client.login(username, password);
    }

    /**
     * Checks if a username is available.
     *
     * @param username the username to check.
     * @return true if the username is available, false otherwise.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public boolean isAvailable(String username) throws RemoteException {
        return client.isAvailable(username);
    }

    /**
     * Signs up a new user.
     *
     * @param username the username.
     * @param password the password.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public void signUp(String username, byte[] password) throws RemoteException {
        client.signUp(username, password);
    }

    /**
     * Changes the username.
     *
     * @param newUsername the new username.
     * @param password    the password.
     * @return true if the username was changed successfully, false otherwise.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public boolean changeUsername(String newUsername, byte[] password) throws RemoteException {
        return client.changeUsername(newUsername, password);
    }

    /**
     * Changes the password.
     *
     * @param oldPassword the old password.
     * @param newPassword the new password.
     * @return true if the password was changed successfully, false otherwise.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public boolean changePassword(byte[] oldPassword, byte[] newPassword) throws RemoteException {
        return client.changePassword(oldPassword, newPassword);
    }

    /**
     * Deletes the user.
     *
     * @param password the password.
     * @return true if the user was deleted successfully, false otherwise.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public boolean deleteUser(byte[] password) throws RemoteException {
        return client.deleteUser(password);
    }

    /**
     * Gets all registered users.
     *
     * @return an array of usernames.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public String[] getAllUsers() throws RemoteException {
        return client.getAllUsers();
    }

    /**
     * Gets the total number of users.
     *
     * @return the total number of users.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public int totalUsers() throws RemoteException {
        return client.totalUsers();
    }

    /**
     * Echoes a message to the server.
     *
     * @param message the message to echo.
     * @return the server's response.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public String echo(@Nullable String message) throws RemoteException {
        return client.echo(message);
    }

    /**
     * Adds a PropertyChangeListener.
     *
     * @param listener the listener to add.
     */
    @Override
    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener.
     *
     * @param listener the listener to remove.
     */
    @Override
    public void removeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Makes a RemoteListener listen to this instance.
     *
     * @param toListen the listener to add.
     */
    @Override
    public void listenTo(@NotNull RemoteListener toListen) {
        toListen.addListener(this);
    }

    /**
     * Closes the session and removes listeners.
     *
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public void close() throws RemoteException {
        remoteListener.removeListener(this, session);
        client.removeListener(null);
        client.close();
    }

    /**
     * Called when a bound property is changed.
     *
     * @param evt a PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt);
    }

    /**
     * Adds a song to a playlist.
     *
     * @param playlistId the playlist ID.
     * @param song       the song to add.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public void addSongToPlaylist(int playlistId, Song song) throws RemoteException {
        Playlist playlist = playlists.get(playlistId);
        if (playlist != null) {
            playlist.addSong(song);
        }
    }

    /**
     * Removes a song from a playlist.
     *
     * @param playlistId the playlist ID.
     * @param song       the song to remove.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public void removeSongFromPlaylist(int playlistId, Song song) throws RemoteException {
        Playlist playlist = playlists.get(playlistId);
        if (playlist != null) {
            playlist.removeSong(song);
        }
    }

    /**
     * Gets all songs from a playlist.
     *
     * @param playlistId the playlist ID.
     * @return a list of songs in the playlist.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public List<Song> getAllSongsFromPlaylist(int playlistId) throws RemoteException {
        Playlist playlist = playlists.get(playlistId);
        return playlist != null ? new ArrayList<>(playlist.getSongs()) : new ArrayList<>();
    }

    /**
     * Creates a new playlist.
     *
     * @param id            the playlist ID.
     * @param title         the playlist title.
     * @param owner         the playlist owner.
     * @param collaborators a list of collaborators.
     * @param creationDate  the creation date.
     * @param songsCount    the number of songs.
     * @param isPublic      whether the playlist is public.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public void createPlaylist(int id, String title, String owner, List<String> collaborators, Date creationDate, int songsCount, boolean isPublic) throws RemoteException {
        Playlist newPlaylist = new Playlist(id, songService, title, owner, creationDate, songsCount, isPublic);
        playlists.put(id, newPlaylist);
    }

    /**
     * Deletes a playlist.
     *
     * @param id the playlist ID.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public void deletePlaylist(int id) throws RemoteException {
        playlists.remove(id);
    }

    /**
     * Gets a playlist by ID.
     *
     * @param id the playlist ID.
     * @return the playlist, or null if not found.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public Playlist getPlaylist(int id) throws RemoteException {
        return playlists.get(id);
    }

    /**
     * Gets all playlists.
     *
     * @return a list of all playlists.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public List<Playlist> getAllPlaylists() throws RemoteException {
        return new ArrayList<>(playlists.values());
    }

    /**
     * Gets the names of all playlists.
     *
     * @return a list of playlist names.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public List<String> getPlaylistNames() throws RemoteException {
        List<String> names = new ArrayList<>();
        playlists.values().forEach(playlist -> names.add(playlist.getTitle()));
        return names;
    }

    /**
     * Gets all songs from the SongService.
     *
     * @return a list of all songs.
     * @throws RemoteException if there is an RMI connection error.
     */
    @Override
    public List<Song> getAllSongs() throws RemoteException {
        return songService.getAllSongs();
    }

    /**
     * Loads pre-created playlists from the SongService.
     *
     * @throws RemoteException if there is an RMI connection error.
     */
    private void loadPreCreatedPlaylists() throws RemoteException {
        List<Playlist> preCreatedPlaylists = songService.getAllPlaylists();
        for (Playlist playlist : preCreatedPlaylists) {
            playlists.put(playlist.getId(), playlist);
        }
    }
}
