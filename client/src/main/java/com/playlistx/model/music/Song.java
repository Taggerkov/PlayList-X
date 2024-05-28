package com.playlistx.model.music;

import java.util.List;
import com.playlistx.model.proxy.SongService;
import java.rmi.RemoteException;

/**
 * Represents a single song or track in the music library.
 */
public class Song {
    private int id;
    private String artist;
    private int year;
    private String genre;
    private String title;
    private String albumName;
    private String link;
    private int duration; // Duration of the song in seconds
    private String featuredArtists; // Additional artists featured in the song
    private List<Song> songs;
    private SongService songService;

    /**
     * Constructs a Song object by fetching song details from the SongService using the song title.
     *
     * @param songTitle The title of the song to fetch details for.
     * @param songService The service used to fetch song details.
     */
    public Song(String songTitle, SongService songService) {
        this.songService = songService;
        try {
            Song song = songService.getSongByTitle(songTitle);
            this.id = song.getId();
            this.artist = song.getArtist();
            this.year = song.getYear();
            this.genre = song.getGenre();
            this.title = song.getTitle();
            this.albumName = song.getAlbumName();
            this.link = song.getLink();
            this.duration = song.getDuration();
            this.featuredArtists = song.getFeaturedArtists();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a list of all songs.
     *
     * @return A list of all songs.
     */
    public List<Song> getAllSongs() {
        return this.songs;
    }

    // Getters and setters for all fields

    /**
     * Gets the ID of the song.
     *
     * @return The ID of the song.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the song.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the artist of the song.
     *
     * @return The artist of the song.
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Sets the artist of the song.
     *
     * @param artist The artist to set.
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Gets the release year of the song.
     *
     * @return The release year of the song.
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the release year of the song.
     *
     * @param year The release year to set.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the genre of the song.
     *
     * @return The genre of the song.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the song.
     *
     * @param genre The genre to set.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the title of the song.
     *
     * @return The title of the song.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the song.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the album name of the song.
     *
     * @return The album name of the song.
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Sets the album name of the song.
     *
     * @param albumName The album name to set.
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * Gets the link to the song.
     *
     * @return The link to the song.
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the link to the song.
     *
     * @param link The link to set.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Gets the duration of the song.
     *
     * @return The duration of the song in seconds.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the song.
     *
     * @param duration The duration to set in seconds.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the featured artists of the song.
     *
     * @return The featured artists of the song.
     */
    public String getFeaturedArtists() {
        return featuredArtists;
    }

    /**
     * Sets the featured artists of the song.
     *
     * @param featuredArtists The featured artists to set.
     */
    public void setFeaturedArtists(String featuredArtists) {
        this.featuredArtists = featuredArtists;
    }

    /**
     * Returns a string representation of the song.
     *
     * @return A string containing the song's details.
     */
    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", artist='" + artist + '\'' +
                ", year=" + year +
                ", genre='" + genre + '\'' +
                ", title='" + title + '\'' +
                ", albumName='" + albumName + '\'' +
                ", link='" + link + '\'' +
                ", duration=" + duration + " seconds" +
                ", featuredArtists='" + featuredArtists + '\'' +
                '}';
    }
}
