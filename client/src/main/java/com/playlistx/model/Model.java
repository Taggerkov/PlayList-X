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
    static Model get() throws RemoteException, NotBoundException {
        return ModelManager.get();
    }

    String login(String string, byte[] hashWord) throws RemoteException, LoginException;

    boolean isAvailable(String generatedName) throws RemoteException;

    void signUp(String string, byte[] hashWord) throws RemoteException;

    boolean changeUsername(String newUsername, byte[] hashWord) throws RemoteException;

    boolean changePassword(byte[] oldHashWord, byte[] newHashWord) throws RemoteException;

    boolean deleteUser(byte[] hashWord) throws RemoteException;

    String[] getAllUsers() throws RemoteException;

    int totalUsers() throws RemoteException;

    String echo(@Nullable String msg) throws RemoteException;

    void addListener(PropertyChangeListener listener) throws RemoteException;

    void removeListener(PropertyChangeListener listener) throws RemoteException;

    void listenTo(@NotNull RemoteListener toListen);

    void close() throws RemoteException;

    // Song management methods
    void addSongToPlaylist(int playlistId, Song song) throws RemoteException;

    void removeSongFromPlaylist(int playlistId, Song song) throws RemoteException;

    List<Song> getAllSongsFromPlaylist(int playlistId) throws RemoteException;

    // Playlist management methods
    void createPlaylist(int id, String title, String owner, List<String> collaborators,
                        Date creationDate, int songsCount, boolean isPublic) throws RemoteException;

    void deletePlaylist(int id) throws RemoteException;

    Playlist getPlaylist(int id) throws RemoteException;

    List<Playlist> getAllPlaylists() throws RemoteException;
}