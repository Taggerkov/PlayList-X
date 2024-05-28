package com.playlistx.model.music;

import com.playlistx.model.proxy.SongService;
import java.rmi.RemoteException;

/**
 * Represents the relationship between a song and a playlist in the music library.
 */
public class SongList {
    private SongService songService;  // The SongService to interact with the server.
    private int songId;  // The unique identifier for a song.
    private int playlistId;  // The unique identifier for a playlist.

    /**
     * Constructs a SongList object to represent the relationship between a song and a playlist.
     *
     * @param songTitle    the title of the song in the relationship.
     * @param playlistName the name of the playlist in the relationship.
     * @param songService  the service used to fetch song and playlist details.
     */
    public SongList(String songTitle, String playlistName, SongService songService) {
        this.songService = songService;
        try {
            this.songId = songService.getSongId(songTitle);
            this.playlistId = songService.getPlaylistId(playlistName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the song ID.
     *
     * @return the ID of the song.
     */
    public int getSongId() {
        return songId;
    }

    /**
     * Gets the playlist ID.
     *
     * @return the ID of the playlist.
     */
    public int getPlaylistId() {
        return playlistId;
    }

    /**
     * Returns a string representation of the SongList object.
     *
     * @return A string describing the song ID and playlist ID association.
     */
    @Override
    public String toString() {
        return "SongList{" +
                "songId=" + songId +
                ", playlistId=" + playlistId +
                '}';
    }
}
