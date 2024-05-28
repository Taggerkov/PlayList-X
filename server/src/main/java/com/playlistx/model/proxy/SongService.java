package com.playlistx.model.proxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import com.playlistx.model.music.Song;
import com.playlistx.model.music.SongList;
import com.playlistx.model.music.Playlist;

/**
 * The SongService interface provides methods for managing songs and playlists.
 */
public interface SongService extends Remote {
    /**
     * Fetches all songs from the database.
     *
     * @return A list of all songs.
     * @throws RemoteException RMI Connection Error.
     */
    List<Song> getAllSongs() throws RemoteException;

    /**
     * Fetches songs associated with a specific playlist from the database.
     *
     * @param playlistId The ID of the playlist.
     * @return A list of songs associated with the specified playlist.
     * @throws RemoteException RMI Connection Error.
     */
    List<Song> getSongsFromPlaylist(int playlistId) throws RemoteException;

    /**
     * Fetches the count of songs associated with a specific playlist from the database.
     *
     * @param playlistId The ID of the playlist.
     * @return The count of songs associated with the specified playlist.
     * @throws RemoteException RMI Connection Error.
     */
    int getSongsCount(int playlistId) throws RemoteException;

    /**
     * Fetches the collaborators of a specific playlist from the database.
     *
     * @param playlistId The ID of the playlist.
     * @return A list of collaborators associated with the specified playlist.
     * @throws RemoteException RMI Connection Error.
     */
    List<String> getCollaborators(int playlistId) throws RemoteException;

    /**
     * Fetches all songs associated with a specific playlist from the database.
     *
     * @param playlistId The ID of the playlist.
     * @return A list of all songs associated with the specified playlist.
     * @throws RemoteException RMI Connection Error.
     */
    List<Song> getAllSongsFromPlaylist(int playlistId) throws RemoteException;

    /**
     * Adds a song to a specific playlist in the database.
     *
     * @param playlistId The ID of the playlist.
     * @param song       The song to be added to the playlist.
     * @throws RemoteException RMI Connection Error.
     */
    void addSongToPlaylist(int playlistId, Song song) throws RemoteException;

    /**
     * Removes a song from a specific playlist in the database.
     *
     * @param playlistId The ID of the playlist.
     * @param song       The song to be removed from the playlist.
     * @throws RemoteException RMI Connection Error.
     */
    void removeSongFromPlaylist(int playlistId, Song song) throws RemoteException;

    /**
     * Fetches the most liked songs from the database.
     *
     * @return A list of the most liked songs.
     * @throws RemoteException RMI Connection Error.
     */
    List<Song> getMostLikedSongs() throws RemoteException;

    /**
     * Fetches the ID of a playlist by its name from the database.
     *
     * @param playlistName The name of the playlist.
     * @return The ID of the playlist.
     * @throws RemoteException RMI Connection Error.
     */
    int getPlaylistId(String playlistName) throws RemoteException;

    /**
     * Fetches a list of song lists from the database.
     *
     * @return A list of song lists.
     * @throws RemoteException RMI Connection Error.
     */
    List<SongList> getSongList() throws RemoteException;

    /**
     * Fetches the ID of a song by its title from the database.
     *
     * @param songTitle The title of the song.
     * @return The ID of the song.
     * @throws RemoteException RMI Connection Error.
     */
    int getSongId(String songTitle) throws RemoteException;

    /**
     * Fetches a song by its title from the database.
     *
     * @param songTitle The title of the song.
     * @return The song.
     * @throws RemoteException RMI Connection Error.
     */
    Song getSongByTitle(String songTitle) throws RemoteException;

    /**
     * Fetches all genres from the database.
     *
     * @return A list of all genres.
     * @throws RemoteException RMI Connection Error.
     */
    List<String> getAllGenres() throws RemoteException;

    /**
     * Fetches the top liked songs by genre from the database.
     *
     * @param genre The genre of the songs.
     * @param limit The maximum number of songs to fetch.
     * @return A list of the top liked songs by genre.
     * @throws RemoteException RMI Connection Error.
     */
    List<Song> getTopLikedSongsByGenre(String genre, int limit) throws RemoteException;

    /**
     * Fetches all playlists from the database.
     *
     * @return A list of all playlists.
     * @throws RemoteException RMI Connection Error.
     */
    List<Playlist> getAllPlaylists() throws RemoteException;

}
