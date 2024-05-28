package com.playlistx.model.music;

/**
 * This interface defines methods for managing songs in a playlist.
 */
public interface Manageable {
    /**
     * Adds a song to the playlist.
     * @param song The song to be added.
     */
    void addSong(Song song);

    /**
     * Removes a song from the playlist.
     * @param song The song to be removed.
     */
    void removeSong(Song song);

    /**
     * Renames the playlist.
     * @param newName The new name for the playlist.
     */
    void renamePlaylist(String newName);
}