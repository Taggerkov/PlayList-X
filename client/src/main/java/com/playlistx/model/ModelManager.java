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
import com.playlistx.model.database.SongDAO;
import com.playlistx.model.music.Suggestion;
import com.playlistx.model.music.SongList;
import com.playlistx.model.music.Listener;
import com.playlistx.model.database.PlaylistDAO;

public class ModelManager implements Model, PropertyChangeListener {
    private static ModelManager instance;
    private final Session session;
    private final Client client;
    private final RemoteListener remoteListener;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private Map<Integer, Playlist> playlists = new HashMap<>();
    private final Suggestion suggestion;
    private SongDAO songDAO;
    private PlaylistDAO playlistDAO = new PlaylistDAO();

    private ModelManager() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1099);
        session = (Session) registry.lookup("session");
        client = (Client) registry.lookup(session.getClient());
        remoteListener = new RemoteListener(this, session);
        songDAO = new SongDAO();
        this.suggestion = new Suggestion(songDAO);
        loadPreCreatedPlaylists();
    }

    public static Model get() throws RemoteException, NotBoundException {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }

    public Suggestion getSuggestion() {
        return suggestion;
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt);
    }

    @Override
    public void addSongToPlaylist(int playlistId, Song song) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist != null) {
            playlist.addSong(song);
        }
    }
    @Override
    public void removeSongFromPlaylist(int playlistId, Song song) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist != null) {
            playlist.removeSong(song);
        }
    }


    @Override
    public List<Song> getAllSongsFromPlaylist(int playlistId) {
        Playlist playlist = playlists.get(playlistId);
        return playlist != null ? new ArrayList<>(playlist.getSongs()) : new ArrayList<>();
    }
    @Override
    public void createPlaylist(int id, String title, int ownerid, List<String> collaborators, Date creationDate, int songsCount, boolean isPublic) throws RemoteException {
        Playlist newPlaylist = new Playlist(id, songDAO, playlistDAO, title, ownerid, creationDate, songsCount, isPublic);
        playlists.put(id, newPlaylist);
    }

    public void loadPreCreatedPlaylists() throws RemoteException {
        List<Playlist> preCreatedPlaylists = playlistDAO.getAllPlaylists();
        for (Playlist playlist : preCreatedPlaylists) {
            playlists.put(playlist.getId(), playlist);
        }
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

    public List<Song> getSongsFromPlaylist(int playlistId) {
        return songDAO.getSongsFromPlaylist(playlistId);
    }

    public int getSongsCount(int playlistId) {
        return songDAO.getSongsCount(playlistId);
    }

    public List<String> getCollaborators(int playlistId) {
        return songDAO.getCollaborators(playlistId);
    }


    public List<Song> getMostLikedSongs() {
        return songDAO.getMostLikedSongs();
    }

    public int getPlaylistId(String playlistName) {
        return playlistDAO.getPlaylistId(playlistName);
    }

    public List<SongList> getSongList() {
        return songDAO.getSongList();
    }

    public int getSongId(String songTitle) {
        return songDAO.getSongId(songTitle);
    }

    public Song getSongByTitle(String songTitle) {
        return songDAO.getSongByTitle(songTitle);
    }

    public List<String> getAllGenres() {
        return songDAO.getAllGenres();
    }

    public List<Song> getTopLikedSongsByGenre(String genre, int limit) {
        return songDAO.getTopLikedSongsByGenre(genre, limit);
    }

    public List<Playlist> getTopLikedPlaylistsByGenre(String genre, int limit) {
        return playlistDAO.getTopLikedPlaylistsByGenre(genre, limit);
    }

    public Listener getListenerById(int id) {
        return songDAO.getListenerById(id);
    }
}