package com.playlistx.model;

import com.playlistx.model.login.LoginException;
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
import com.playlistx.model.music.Playlist;
import com.playlistx.model.music.Song;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.playlistx.model.music.SongDAO;



public class ModelManager implements Model, PropertyChangeListener {
    private static ModelManager instance;
    private final Session session;
    private final Client client;
    private final RemoteListener remoteListener;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private Map<Integer, Playlist> playlists = new HashMap<>();
    private SongDAO songDAO; // Add a SongDAO to interact with the database

    private ModelManager() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1099);
        session = (Session) registry.lookup("session");
        client = (Client) registry.lookup(session.getClient());
        remoteListener = new RemoteListener(this, session);
        songDAO = new SongDAO(); // Initialize the SongDAO
    }

    public static Model get() throws RemoteException, NotBoundException {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }


    @Override
    public String login(String string, byte[] hashWord) throws RemoteException, LoginException {
        return client.login(string, hashWord);
    }

    @Override
    public boolean isAvailable(String generatedName) throws RemoteException {
        return client.isAvailable(generatedName);
    }

    @Override
    public void signUp(String string, byte[] hashWord) throws RemoteException {
        client.signUp(string, hashWord);
    }

    @Override
    public boolean changeUsername(String newUsername, byte[] hashWord) throws RemoteException {
        return client.changeUsername(newUsername, hashWord);
    }

    @Override
    public boolean changePassword(byte[] oldHashWord, byte[] newHashWord) throws RemoteException {
        return client.changePassword(oldHashWord, newHashWord);
    }

    @Override
    public boolean deleteUser(byte[] hashWord) throws RemoteException {
        return client.deleteUser(hashWord);
    }

    @Override
    public String[] getAllUsers() throws RemoteException {
        return client.getAllUsers();
    }

    @Override
    public int totalUsers() throws RemoteException {
        return client.totalUsers();
    }

    @Override
    public String echo(@Nullable String msg) throws RemoteException {
        return client.echo(msg);
    }

    @Override
    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void removeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public void listenTo(@NotNull RemoteListener toListen) {
        toListen.addListener(this);
    }

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
        support.firePropertyChange(evt);
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

