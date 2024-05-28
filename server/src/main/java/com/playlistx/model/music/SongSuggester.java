package com.playlistx.model.music;

/**
 * The SongSuggester interface provides a method for suggesting a song based on a playlist.
 */
public interface SongSuggester {
    java.util.List<Song> suggestSong(Playlist playlist);
}