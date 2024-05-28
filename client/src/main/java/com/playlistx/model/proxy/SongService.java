package com.playlistx.model.proxy;

import com.playlistx.model.music.Playlist;
import com.playlistx.model.music.Song;
import com.playlistx.model.music.SongList;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * The SongService interface provides methods for managing songs and playlists.
 * It extends the java.rmi.Remote interface, enabling remote method calls.
 */
public interface SongService extends Remote {

    /**
     * Retrieves all songs.
     *
     * @return a list of all songs.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<Song> getAllSongs() throws RemoteException;

    /**
     * Retrieves all songs from a specified playlist.
     *
     * @param playlistId the ID of the playlist.
     * @return a list of songs in the specified playlist.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<Song> getSongsFromPlaylist(int playlistId) throws RemoteException;

    /**
     * Gets the number of songs in a specified playlist.
     *
     * @param playlistId the ID of the playlist.
     * @return the number of songs in the playlist.
     * @throws RemoteException if there is an RMI connection error.
     */
    int getSongsCount(int playlistId) throws RemoteException;

    /**
     * Retrieves the collaborators of a specified playlist.
     *
     * @param playlistId the ID of the playlist.
     * @return a list of collaborators for the playlist.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<String> getCollaborators(int playlistId) throws RemoteException;

    /**
     * Retrieves all songs from a specified playlist.
     *
     * @param playlistId the ID of the playlist.
     * @return a list of songs in the specified playlist.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<Song> getAllSongsFromPlaylist(int playlistId) throws RemoteException;

    /**
     * Adds a song to a specified playlist.
     *
     * @param playlistId the ID of the playlist.
     * @param song       the song to add.
     * @throws RemoteException if there is an RMI connection error.
     */
    void addSongToPlaylist(int playlistId, Song song) throws RemoteException;

    /**
     * Removes a song from a specified playlist.
     *
     * @param playlistId the ID of the playlist.
     * @param song       the song to remove.
     * @throws RemoteException if there is an RMI connection error.
     */
    void removeSongFromPlaylist(int playlistId, Song song) throws RemoteException;

    /**
     * Retrieves the most liked songs.
     *
     * @return a list of the most liked songs.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<Song> getMostLikedSongs() throws RemoteException;

    /**
     * Gets the ID of a playlist by its name.
     *
     * @param playlistName the name of the playlist.
     * @return the ID of the playlist.
     * @throws RemoteException if there is an RMI connection error.
     */
    int getPlaylistId(String playlistName) throws RemoteException;

    /**
     * Retrieves a list of song lists.
     *
     * @return a list of song lists.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<SongList> getSongList() throws RemoteException;

    /**
     * Gets the ID of a song by its title.
     *
     * @param songTitle the title of the song.
     * @return the ID of the song.
     * @throws RemoteException if there is an RMI connection error.
     */
    int getSongId(String songTitle) throws RemoteException;

    /**
     * Retrieves a song by its title.
     *
     * @param songTitle the title of the song.
     * @return the song with the specified title.
     * @throws RemoteException if there is an RMI connection error.
     */
    Song getSongByTitle(String songTitle) throws RemoteException;

    /**
     * Retrieves all music genres.
     *
     * @return a list of all music genres.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<String> getAllGenres() throws RemoteException;

    /**
     * Retrieves the top liked songs in a specified genre.
     *
     * @param genre the genre.
     * @param limit the maximum number of songs to retrieve.
     * @return a list of the top liked songs in the specified genre.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<Song> getTopLikedSongsByGenre(String genre, int limit) throws RemoteException;

    /**
     * Retrieves all playlists.
     *
     * @return a list of all playlists.
     * @throws RemoteException if there is an RMI connection error.
     */
    List<Playlist> getAllPlaylists() throws RemoteException;
}
