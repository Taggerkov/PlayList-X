package com.playlistx.model.music;
import com.playlistx.model.database.SongDAO;

public class Suggestion implements com.playlistx.model.music.SongSuggester {
    private String label;
    private java.util.List<Song> suggestedSongs;
    private final java.util.Random random = new java.util.Random();
    private SongDAO songDAO;

    public Suggestion(SongDAO songDAO) {
        this.songDAO = songDAO;
    }

    public String getLabel() {
        return label;
    }
    @Override
    public java.util.List<Song> suggestSong(com.playlistx.model.music.Playlist playlist) {
        // Fetch all unique genres from the database
        java.util.List<String> genres = songDAO.getAllGenres();
        if (genres.isEmpty()) {
            return null; // or throw an exception
        }

        // Randomly select a genre
        String selectedGenre = genres.get(random.nextInt(genres.size()));

        // Fetch the top 3 liked songs from the selected genre
        java.util.List<Song> songs = songDAO.getTopLikedSongsByGenre(selectedGenre, 3);
        if (songs.isEmpty()) {
            return null; // or throw an exception
        }

        // Return the top 3 songs from the selected genre
        return songs;
    }
}