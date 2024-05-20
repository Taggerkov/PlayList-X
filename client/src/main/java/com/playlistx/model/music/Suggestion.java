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

    public String getLabel() {
        return label;
    }
    @Override
    public java.util.List<Song> suggestSong(Playlist playlist) {
        // Fetch all unique genres from the database
        List<String> genres = songDAO.getAllGenres();
        if (genres.isEmpty()) {
            return null; // or throw an exception
        }

        // Randomly select a genre
        String selectedGenre = genres.get(random.nextInt(genres.size()));

        // Fetch the top 3 liked songs from the selected genre
        List<Song> songs = songDAO.getTopLikedSongsByGenre(selectedGenre, 3);
        if (songs.isEmpty()) {
            return null; // or throw an exception
        }

        // Return the top 3 songs from the selected genre
        return songs;
    }
}