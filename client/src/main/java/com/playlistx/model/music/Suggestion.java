package com.playlistx.model.music;

import com.playlistx.model.proxy.SongService;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;

/**
 * Implements the SongSuggester interface to provide song suggestions based on genres.
 */
public class Suggestion implements SongSuggester {
    private String label; // Label for the suggestion
    private List<Song> suggestedSongs; // List of suggested songs
    private final Random random = new Random(); // Random instance for selecting genres
    private SongService songService; // Service for interacting with the server

    /**
     * Constructs a Suggestion object with the specified SongService.
     *
     * @param songService the service used to fetch song and genre details.
     */
    public Suggestion(SongService songService) {
        this.songService = songService;
    }

    /**
     * Gets the label of the suggestion.
     *
     * @return the label of the suggestion.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Suggests a list of songs for a given playlist.
     *
     * @param playlist the playlist for which to suggest songs.
     * @return a list of suggested songs, or null if no suggestions are available.
     */
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
