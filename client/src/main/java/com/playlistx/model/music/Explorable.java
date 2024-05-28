package com.playlistx.model.music;
import java.util.List;



/**
 * This interface defines methods for exploring songs and playlists.
 */
public interface Explorable {
    /**
     * Explores available songs.
     * @return A list of songs.
     */
    List<Song> exploreSongs();

    /**
     * Explores available playlists.
     * @return A list of playlists.
     */
    List<Playlist> explorePlaylists();
}