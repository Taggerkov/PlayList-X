package com.playlistx.model;

import com.playlistx.model.login.LoginException;
import com.playlistx.model.music.Playlist;
import com.playlistx.model.music.Song;
import com.playlistx.model.music.SongDAO;
import com.playlistx.model.proxy.Client;
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
import java.util.*;

/**
 * Local class, handler of client side logic.
 *
 * @author Sergiu Chirap, Raj
 * @version final
 * @since 0.1
 */
public class ModelManager implements Model, PropertyChangeListener {
    /**
     * This is the {@code Singleton} instance.
     */
    private static ModelManager instance;
    /**
     * RMI first layer connection.
     */
    private final Session session;
    /**
     * RMI second layer connection.
     */
    private final Client client;
    /**
     * Remote Observer patter proxy.
     */
    private final RemoteListener remoteListener;
    /**
     * Observer Pattern trigger manager.
     */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final Map<Integer, Playlist> playlists = new HashMap<>();
    private final SongDAO songDAO = new SongDAO();

    /**
     * Private constructor. Sets up connection to server through RMI.
     *
     * @throws RemoteException   RMI Connection Error
     * @throws NotBoundException RMI Connection Error
     * @author Sergiu Chirap
     */
    private ModelManager() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1099);
        session = (Session) registry.lookup("session");
        client = (Client) registry.lookup(session.getClient());
        remoteListener = new RemoteListener(this, session);
    }

    /**
     * {@code Singleton} getter. Gets the singleton instance or creates a new one if none exists.
     *
     * @return The singleton instance.
     */
    public static Model get() throws RemoteException, NotBoundException {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }

    /**
     * Asks server to log client in.
     * <blockquote>
     * <pre>
     *     "LOGIN-USER" -> Invalid username login
     *     "LOGIN-PASSWORD" -> Invalid password login
     *     "SIGNUP-USER" -> Invalid username signup
     *     "SIGNUP-PASSWORD" -> Invalid password signup
     *     "LOGIN" -> Successful login
     *     "SIGNUP" -> Successful password
     *     </pre>
     * </blockquote>
     *
     * @param username User username.
     * @param hashWord User password byte.
     * @return A String containing the result code of this process.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public String login(String username, byte[] hashWord) throws RemoteException, LoginException {
        return client.login(username, hashWord);
    }

    /**
     * Checks if a username is available in the services.
     *
     * @param username The username to check.
     * @return A boolean stating if is available.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public boolean isAvailable(String username) throws RemoteException {
        return client.isAvailable(username);
    }

    /**
     * Signs up a new user in the services.
     *
     * @param username New user username.
     * @param hashWord New user password bytes.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public void signUp(String username, byte[] hashWord) throws RemoteException {
        client.signUp(username, hashWord);
    }

    /**
     * Asks server to change user username for a provided one.
     *
     * @param newUsername User new username.
     * @param hashWord    User password bytes.
     * @return Operation success.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public boolean changeUsername(String newUsername, byte[] hashWord) throws RemoteException {
        return client.changeUsername(newUsername, hashWord);
    }

    /**
     * Asks server to change user password for a provided one.
     *
     * @param oldHashWord User password bytes.
     * @param newHashWord User new password bytes.
     * @return Operation success.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public boolean changePassword(byte[] oldHashWord, byte[] newHashWord) throws RemoteException {
        return client.changePassword(oldHashWord, newHashWord);
    }

    /**
     * Deletes user from the services.
     *
     * @param hashWord User password bytes.
     * @return Operation success.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public boolean deleteUser(byte[] hashWord) throws RemoteException {
        return client.deleteUser(hashWord);
    }

    /**
     * Gets all usernames registered in the service.
     *
     * @return All users usernames.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public String[] getAllUsers() throws RemoteException {
        return client.getAllUsers();
    }

    /**
     * Gets how many users are registered in the service.
     *
     * @return Users registered amount.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public int totalUsers() throws RemoteException {
        return client.totalUsers();
    }

    /**
     * Pings the server. An attached message is optional, else {@code null}.
     *
     * @param msg Optional message attached to ping.
     * @return The response from the server.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public String echo(@Nullable String msg) throws RemoteException {
        return client.echo(msg);
    }

    /**
     * Adds a {@link PropertyChangeListener} to this class {@link PropertyChangeSupport}.
     *
     * @param listener The listener to be added to this class support.
     * @author Sergiu Chirap
     */
    @Override
    public void addListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Removes a {@link PropertyChangeListener} from this class {@link PropertyChangeSupport}.
     *
     * @param listener The listener to be removed from this class support.
     * @author Sergiu Chirap
     */
    @Override
    public void removeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Make a {@link PropertyChangeListener listener} listen to this instance.
     *
     * @param toListen Listener to add to instance's {@link java.beans.PropertyChangeSupport support}.
     * @author Sergiu Chirap
     */
    @Override
    public void listenTo(@NotNull RemoteListener toListen) {
        toListen.addListener(this);
    }

    /**
     * Successfully logs user out from session.
     *
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public void close() throws RemoteException {
        remoteListener.removeListener(this, session);
        client.removeListener(null);
        client.close();
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

    // Playlist and song management implementations:
    @Override
    public void addSongToPlaylist(int playlistId, Song song) throws RemoteException {
        Playlist playlist = playlists.get(playlistId);
        if (playlist != null) {
            playlist.addSong(song);
        }
    }

    @Override
    public void removeSongFromPlaylist(int playlistId, Song song) throws RemoteException {
        Playlist playlist = playlists.get(playlistId);
        if (playlist != null) {
            playlist.removeSong(song);
        }
    }

    @Override
    public List<Song> getAllSongsFromPlaylist(int playlistId) throws RemoteException {
        Playlist playlist = playlists.get(playlistId);
        return playlist != null ? new ArrayList<>(playlist.getSongs()) : new ArrayList<>();
    }

    @Override
    public void createPlaylist(int id, String title, String owner, List<String> collaborators, Date creationDate, int songsCount, boolean isPublic) throws RemoteException {
        Playlist newPlaylist = new Playlist(id, songDAO, title, owner, collaborators, creationDate, songsCount, isPublic); // Pass the SongDAO to the Playlist constructor
        playlists.put(id, newPlaylist);
    }

    @Override
    public void deletePlaylist(int id) throws RemoteException {
        playlists.remove(id);
    }

    @Override
    public Playlist getPlaylist(int id) throws RemoteException {
        return playlists.get(id);
    }

    @Override
    public List<Playlist> getAllPlaylists() throws RemoteException {
        return new ArrayList<>(playlists.values());
    }

    @Override
    public List<String> getPlaylistNames() throws RemoteException {
        List<String> names = new ArrayList<>();
        playlists.values().forEach(playlist -> names.add(playlist.getTitle()));
        return names;
    }

    @Override
    public List<Song> getAllSongs() throws java.rmi.RemoteException {
        return songDAO.getAllSongs();
    }
}