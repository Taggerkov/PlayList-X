package com.playlistx.model.music;

import java.util.List;
import java.util.Random;

/**
 * The Suggestion class represents a suggestion for songs based on various criteria.
 * It implements the SongSuggester interface to suggest a song from a playlist.
 */
public class Suggestion implements SongSuggester {
    private String label; // Label to indicate the type of suggestion (e.g., "Popular", "New", "Similar")
    private List<Song> suggestedSongs; // List of songs suggested
    private final Random random = new Random();

    // existing constructors, getters, and setters...

    @Override
    public Song suggestSong(Playlist playlist) {
        List<Song> songs = playlist.getSongs();
        if (songs.isEmpty()) {
            return null; // or throw an exception
        }
        int randomIndex = random.nextInt(songs.size());
        return songs.get(randomIndex);
    }
}