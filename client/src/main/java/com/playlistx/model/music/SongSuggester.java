package com.playlistx.model.music;

import java.util.List;

/**
 * This interface defines methods for suggesting songs based on a playlist.
 */
public interface SongSuggester {
    /**
     * Suggests a list of songs based on the given playlist.
     * @param playlist The playlist to base the suggestions on.
     * @return A list of suggested songs.
     */
    List<Song> suggestSong(Playlist playlist);
}