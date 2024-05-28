package com.playlistx.model.music;

import com.playlistx.model.proxy.SongService;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;

public class Suggestion implements SongSuggester {
    private String label;
    private List<Song> suggestedSongs;
    private final Random random = new Random();
    private SongService songService;

    public Suggestion(SongService songService) {
        this.songService = songService;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public List<Song> suggestSong(Playlist playlist) {
        List<String> genres;
        try {
            // Fetch all unique genres from the server
            genres = songService.getAllGenres();
            if (genres.isEmpty()) {
                return null; // or throw an exception
            }

            // Randomly select a genre
            String selectedGenre = genres.get(random.nextInt(genres.size()));

            // Fetch the top 3 liked songs from the selected genre
            List<Song> songs = songService.getTopLikedSongsByGenre(selectedGenre, 3);
            if (songs.isEmpty()) {
                return null; // or throw an exception
            }

            // Return the top 3 songs from the selected genre
            return songs;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
}