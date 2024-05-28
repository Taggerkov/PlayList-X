package com.playlistx.model.proxy;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import com.playlistx.model.music.Song;
import com.playlistx.model.database.SongDAO;
import com.playlistx.model.music.Playlist;
import com.playlistx.model.music.SongList;

/**
 * The implementation of the SongService interface that provides methods for managing songs and playlists.
 */
public class SongServiceImpl extends UnicastRemoteObject implements SongService {
    private SongDAO songDAO;

    /**
     * Constructs a SongServiceImpl instance.
     * @throws RemoteException RMI Connection Error.
     */
    public SongServiceImpl() throws RemoteException {
        songDAO = new SongDAO();
    }

    /**
     * Fetches all songs from the database.
     * @return A list of all songs.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public List<Song> getAllSongs() throws RemoteException {
        return songDAO.getAllSongs();
    }

    /**
     * Fetches all songs belonging to a playlist from the database.
     * @param playlistId The ID of the playlist.
     * @return A list of all songs in the specified playlist.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public List<Song> getSongsFromPlaylist(int playlistId) throws RemoteException {
        return songDAO.getSongsFromPlaylist(playlistId);
    }

    /**
     * Retrieves the count of songs in a playlist from the database.
     * @param playlistId The ID of the playlist.
     * @return The number of songs in the playlist.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public int getSongsCount(int playlistId) throws RemoteException {
        return songDAO.getSongsCount(playlistId);
    }

    /**
     * Retrieves the collaborators of a playlist from the database.
     * @param playlistId The ID of the playlist.
     * @return A list of collaborators' usernames.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public List<String> getCollaborators(int playlistId) throws RemoteException {
        return songDAO.getCollaborators(playlistId);
    }

    /**
     * Fetches all songs from a playlist in the database.
     * @param playlistId The ID of the playlist.
     * @return A list of all songs in the specified playlist.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public List<Song> getAllSongsFromPlaylist(int playlistId) throws RemoteException {
        return songDAO.getAllSongsFromPlaylist(playlistId);
    }

    /**
     * Adds a song to a playlist in the database.
     * @param playlistId The ID of the playlist.
     * @param song The song to add to the playlist.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public void addSongToPlaylist(int playlistId, Song song) throws RemoteException {
        songDAO.addSongToPlaylist(playlistId, song);
    }

    /**
     * Removes a song from a playlist in the database.
     * @param playlistId The ID of the playlist.
     * @param song The song to remove from the playlist.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public void removeSongFromPlaylist(int playlistId, Song song) throws RemoteException {
        songDAO.removeSongFromPlaylist(playlistId, song);
    }

    /**
     * Fetches the most liked songs from the database.
     * @return A list of the most liked songs.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public List<Song> getMostLikedSongs() throws RemoteException {
        return songDAO.getMostLikedSongs();
    }

    /**
     * Retrieves the ID of a playlist by its name from the database.
     * @param playlistName The name of the playlist.
     * @return The ID of the playlist.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public int getPlaylistId(String playlistName) throws RemoteException {
        return songDAO.getPlaylistId(playlistName);
    }

    /**
     * Retrieves the list of songs and their associated playlists from the database.
     * @return A list of SongList objects.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public List<SongList> getSongList() throws RemoteException {
        return songDAO.getSongList();
    }

    /**
     * Retrieves the ID of a song by its title from the database.
     * @param songTitle The title of the song.
     * @return The ID of the song.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public int getSongId(String songTitle) throws RemoteException {
        return songDAO.getSongId(songTitle);
    }

    /**
     * Retrieves a song by its title from the database.
     * @param songTitle The title of the song.
     * @return The song object.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public Song getSongByTitle(String songTitle) throws RemoteException {
        return songDAO.getSongByTitle(songTitle);
    }

    /**
     * Retrieves all genres from the database.
     * @return A list of all genres.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public List<String> getAllGenres() throws RemoteException {
        return songDAO.getAllGenres();
    }

    /**
     * Retrieves the top liked songs by genre from the database.
     * @param genre The genre of the songs.
     * @param limit The maximum number of songs to retrieve.
     * @return A list of the top liked songs.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public List<Song> getTopLikedSongsByGenre(String genre, int limit) throws RemoteException {
        return songDAO.getTopLikedSongsByGenre(genre, limit);
    }

    /**
     * Fetches all playlists from the database.
     * @return A list of all playlists.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public List<Playlist> getAllPlaylists() throws RemoteException {
        return songDAO.getAllPlaylists();
    }
}