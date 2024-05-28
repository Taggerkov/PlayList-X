package com.playlistx.model.music;
import com.playlistx.model.database.SongDAO;

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
    private java.util.List<Song> songs;
    private SongDAO songDAO;

    /**
     * Constructs a Song instance.
     *
     * @param songTitle the title of the song
     * @param songDAO   the data access object for songs
     */
    public Song(String songTitle, SongDAO songDAO) {
        this.songDAO = songDAO;
        com.playlistx.model.music.Song song = songDAO.getSongByTitle(songTitle);
        this.id = song.getId();
        this.artist = song.getArtist();
        this.year = song.getYear();
        this.genre = song.getGenre();
        this.title = song.getTitle();
        this.albumName = song.getAlbumName();
        this.link = song.getLink();
        this.featuredArtists = song.getFeaturedArtists();
    }

    /**
     * Constructs a Song instance.
     *
     * @param id              the unique identifier for the song
     * @param artist          the artist or band name
     * @param year            the year the song was released
     * @param genre           the genre of the song
     * @param title           the title of the song
     * @param albumName       the name of the album the song belongs to
     * @param link            the URL link to the song
     * @param featuredArtists additional artists featured in the song
     */
    public Song(int id, String artist, int year, String genre, String title, String albumName, String link, String featuredArtists) {
        this.id = id;
        this.artist = artist;
        this.year = year;
        this.genre = genre;
        this.title = title;
        this.albumName = albumName;
        this.link = link;
        this.featuredArtists = featuredArtists;
    }

    /**
     * Adds a song to the list of songs.
     *
     */
    public java.util.List<Song> getAllSongs() {
        return this.songs;
    }

    // Getters and setters for all fields

    /**
     * Gets the unique identifier for the song.
     *
     * @return the unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the song.
     *
     * @param id the unique identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the artist or band name.
     *
     * @return the artist or band name
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Sets the artist or band name.
     *
     * @param artist the artist or band name
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Gets the year the song was released.
     *
     * @return the year the song was released
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the year the song was released.
     *
     * @param year the year the song was released
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the genre of the song.
     *
     * @return the genre of the song
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the song.
     *
     * @param genre the genre of the song
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the title of the song.
     *
     * @return the title of the song
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the song.
     *
     * @param title the title of the song
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the name of the album the song belongs to.
     *
     * @return the name of the album
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Sets the name of the album the song belongs to.
     *
     * @param albumName the name of the album
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * Gets the URL link to the song.
     *
     * @return the URL link
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the URL link to the song.
     *
     * @param link the URL link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Gets the additional artists featured in the song.
     *
     * @return the featured artists
     */
    public String getFeaturedArtists() {
        return featuredArtists;
    }

    /**
     * Sets the additional artists featured in the song.
     *
     * @param featuredArtists the featured artists
     */
    public void setFeaturedArtists(String featuredArtists) {
        this.featuredArtists = featuredArtists;
    }

    /**
     * Gets the duration of the song in seconds.
     *
     * @return the duration of the song
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the song in seconds.
     *
     * @param duration the duration of the song
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Returns a string representation of the song.
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

    /**
     * Gets the featured artist(s) in the song.
     *
     * @return the featured artist(s)
     */
    public String getFeaturedArtist() {
        return featuredArtists;
    }
}
