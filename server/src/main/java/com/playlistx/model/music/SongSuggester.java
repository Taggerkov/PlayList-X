package com.playlistx.model.music;

public interface SongSuggester {
    java.util.List<Song> suggestSong(Playlist playlist);
}