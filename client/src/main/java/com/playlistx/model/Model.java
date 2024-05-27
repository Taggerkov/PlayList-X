package com.playlistx.model;

import com.playlistx.model.music.Playlist;
import com.playlistx.model.music.Song;
import com.playlistx.model.proxy.RemoteListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 * The Model interface is responsible for the handling of the client logic.
 *
 * @author Sergiu Chirap, Raj
 * @version final
 * @since 0.1
 */
public interface Model {
    /**
     * Getter of default model.
     *
     * @return The default or preset model.
     * @throws RemoteException            RMI Connection Error.
     * @throws java.rmi.NotBoundException RMI Connection Error.
     * @author Sergiu Chirap
     */
    static Model get() throws RemoteException, java.rmi.NotBoundException {
        return ModelManager.get();
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
     * @param password User password byte.
     * @return A String containing the result code of this process.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    String login(String username, byte[] password) throws RemoteException;

    /**
     * Checks if a username is available in the services.
     *
     * @param username The username to check.
     * @return A boolean stating if is available.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    boolean isAvailable(String username) throws RemoteException;

    /**
     * Signs up a new user in the services.
     *
     * @param username New user username.
     * @param password New user password bytes.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    void signUp(String username, byte[] password) throws RemoteException;

    /**
     * Asks server to change user username for a provided one.
     *
     * @param newUsername User new username.
     * @param password    User password bytes.
     * @return Operation success.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    boolean changeUsername(String newUsername, byte[] password) throws RemoteException;

    /**
     * Asks server to change user password for a provided one.
     *
     * @param oldPassword User password bytes.
     * @param newPassword User new password bytes.
     * @return Operation success.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    boolean changePassword(byte[] oldPassword, byte[] newPassword) throws RemoteException;

    /**
     * Deletes user from the services.
     *
     * @param password User password bytes.
     * @return Operation success.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    boolean deleteUser(byte[] password) throws RemoteException;

    /**
     * Gets all usernames registered in the service.
     *
     * @return All users usernames.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    String[] getAllUsers() throws RemoteException;

    /**
     * Gets how many users are registered in the service.
     *
     * @return Users registered amount.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    int totalUsers() throws RemoteException;

    /**
     * Pings the server. An attached message is optional, else {@code null}.
     *
     * @param message Optional message attached to ping.
     * @return The response from the server.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    String echo(@Nullable String message) throws RemoteException;

    /**
     * Adds a {@link PropertyChangeListener} to this class {@link PropertyChangeSupport}.
     *
     * @param listener The listener to be added to this class support.
     * @author Sergiu Chirap
     */
    void addListener(PropertyChangeListener listener) throws RemoteException;

    /**
     * Removes a {@link PropertyChangeListener} from this class {@link PropertyChangeSupport}.
     *
     * @param listener The listener to be removed from this class support.
     * @author Sergiu Chirap
     */
    void removeListener(PropertyChangeListener listener) throws RemoteException;

    /**
     * Make a {@link PropertyChangeListener listener} listen to this instance.
     *
     * @param toListen Listener to add to instance's {@link java.beans.PropertyChangeSupport support}.
     * @author Sergiu Chirap
     */
    void listenTo(@NotNull RemoteListener toListen);

    /**
     * Successfully logs user out from session.
     *
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    void close() throws RemoteException;

    // Song and Playlist Management Methods
    void addSongToPlaylist(int playlistId, Song song) throws RemoteException;

    void removeSongFromPlaylist(int playlistId, Song song) throws RemoteException;

    List<Song> getAllSongsFromPlaylist(int playlistId) throws RemoteException;

    void createPlaylist(int id, String title, String owner, List<String> collaborators, Date creationDate, int songsCount, boolean isPublic) throws RemoteException;

    void deletePlaylist(int id) throws RemoteException;

    Playlist getPlaylist(int id) throws RemoteException;

    List<Playlist> getAllPlaylists() throws RemoteException;

    List<String> getPlaylistNames() throws RemoteException;

    List<Song> getAllSongs() throws RemoteException;
}