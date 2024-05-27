package com.playlistx.model.music;

import java.util.List;
import java.util.Random;

public class Suggestion implements SongSuggester {
    private String label;
    private List<Song> suggestedSongs;
    private final Random random = new Random();
    private SongDAO songDAO;

    public Suggestion(SongDAO songDAO) {
        this.songDAO = songDAO;
    }

    // existing constructors, getters, and setters...

    @Override
    public Song suggestSong(Playlist playlist) {
        // Fetch the most liked songs from the database
        List<Song> songs = songDAO.getMostLikedSongs();
        if (songs.isEmpty()) {
            return null; // or throw an exception
        }
        int randomIndex = random.nextInt(songs.size());
        return songs.get(randomIndex);
    }
}