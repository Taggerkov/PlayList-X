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

    /**
     * Adds a song to a playlist and updates the server.
     *
     * @param playlistId the ID of the playlist.
     * @param song       the song to add.
     * @throws RemoteException if there is an RMI connection error.
     */
    void addSongToPlaylist(int playlistId, Song song) throws RemoteException;

    /**
     * Removes a song from a playlist and updates the server.
     *
     * @param playlistId the ID of the playlist.
     * @param song       the song to remove.
     * @throws RemoteException if there is an RMI connection error.
     */
    void removeSongFromPlaylist(int playlistId, Song song) throws RemoteException;

    /**
     * Gets all songs from a playlist.
     *
     * @param playlistId the ID of the playlist.
     * @return a list of all songs in the playlist.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<Song> getAllSongsFromPlaylist(int playlistId) throws RemoteException;

    /**
     * Creates a new playlist.
     *
     * @param id            the unique identifier for the playlist.
     * @param title         the title of the playlist.
     * @param owner         the owner of the playlist.
     * @param collaborators the list of collaborators for the playlist.
     * @param creationDate  the date the playlist was created.
     * @param songsCount    the count of songs currently in the playlist.
     * @param isPublic      a flag indicating if the playlist is public or private.
     * @throws RemoteException if there is an RMI connection error.
     */
    void createPlaylist(int id, String title, String owner, List<String> collaborators, Date creationDate, int songsCount, boolean isPublic) throws RemoteException;

    /**
     * Deletes a playlist.
     *
     * @param id the unique identifier for the playlist to delete.
     * @throws RemoteException if there is an RMI connection error.
     */
    void deletePlaylist(int id) throws RemoteException;

    /**
     * Gets a playlist by its ID.
     *
     * @param id the unique identifier for the playlist.
     * @return the playlist with the specified ID.
     * @throws RemoteException if there is an RMI connection error.
     */
    Playlist getPlaylist(int id) throws RemoteException;

    /**
     * Gets all playlists.
     *
     * @return a list of all playlists.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<Playlist> getAllPlaylists() throws RemoteException;

    /**
     * Gets the names of all playlists.
     *
     * @return a list of all playlist names.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<String> getPlaylistNames() throws RemoteException;

    /**
     * Gets all songs.
     *
     * @return a list of all songs.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<Song> getAllSongs() throws RemoteException;
}