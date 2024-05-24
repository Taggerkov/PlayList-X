package com.playlistx.model.music;

import java.util.List;
import java.util.Random;
import com.playlistx.model.database.SongDAO;
import com.playlistx.model.database.PlaylistDAO;

public class Suggestion implements SongSuggester {
    private String label;
    private List<Song> suggestedSongs;
    private final Random random = new Random();
    private SongDAO songDAO;
    private Playlist playlist;
    private PlaylistDAO playlistDAO;

    public Suggestion(com.playlistx.model.database.SongDAO songDAO) {
        this.songDAO = songDAO;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public List<Song> suggestSong(Playlist playlist) {
        this.playlist = playlist;
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

    public List<Playlist> suggestPlaylist() {
        // Fetch all unique genres from the database
        List<String> genres = songDAO.getAllGenres();
        if (genres.isEmpty()) {
            return null; // or throw an exception
        }

        // Randomly select a genre
        String selectedGenre = genres.get(random.nextInt(genres.size()));

        // Fetch the top 3 liked playlists from the selected genre
        List<Playlist> playlists = playlistDAO.getTopLikedPlaylistsByGenre(selectedGenre, 3);
        if (playlists.isEmpty()) {
            return null; // or throw an exception
        }

        // Return the top 3 playlists from the selected genre
        return playlists;
    }
}