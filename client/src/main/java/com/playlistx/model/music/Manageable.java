package com.playlistx.model.music;

// Define the Manageable interface
public interface Manageable {
    void addSong(Song song);
    void removeSong(Song song);
    void renamePlaylist(String newName);
}