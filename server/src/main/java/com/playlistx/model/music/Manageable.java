package com.playlistx.model.music;

// Define the Manageable interface
public interface Manageable {
    void addSong(com.playlistx.model.music.Song song);
    void removeSong(com.playlistx.model.music.Song song);
    void renamePlaylist(String newName);
}