package com.playlistx.model.music;

/**
 * The Explorable interface provides methods for exploring songs and playlists.
 */
public interface Explorable {
    java.util.List<Song> exploreSongs();
    java.util.List<Playlist> explorePlaylists();

}
