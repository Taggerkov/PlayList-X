package com.playlistx.model;

import com.playlistx.model.login.LoginException;
import com.playlistx.model.proxy.RemoteListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import com.playlistx.model.music.Playlist;
import com.playlistx.model.music.Song;
import java.util.List;
import java.util.Date;


public interface Model {
    static Model get() throws RemoteException, java.rmi.NotBoundException {
        return ModelManager.get();
    }

    String login(String username, byte[] password) throws RemoteException, LoginException;
    boolean isAvailable(String username) throws RemoteException;
    void signUp(String username, byte[] password) throws RemoteException;
    boolean changeUsername(String newUsername, byte[] password) throws RemoteException;
    boolean changePassword(byte[] oldPassword, byte[] newPassword) throws RemoteException;
    boolean deleteUser(byte[] password) throws RemoteException;
    String[] getAllUsers() throws RemoteException;
    int totalUsers() throws RemoteException;
    String echo(@Nullable String message) throws RemoteException;

    void addListener(PropertyChangeListener listener) throws RemoteException;
    void removeListener(PropertyChangeListener listener) throws RemoteException;
    void listenTo(@NotNull RemoteListener toListen) throws RemoteException;
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
}
