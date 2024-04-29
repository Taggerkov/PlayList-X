package com.playlistx.model.music;

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

    /**
     * Constructs a Song with detailed attributes.
     *
     * @param id the unique identifier for the song
     * @param artist the main artist of the song
     * @param year the release year of the song
     * @param genre the musical genre of the song
     * @param title the title of the song
     * @param albumName the name of the album the song is from
     * @param link a URL to the song or its resources
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

    // Getters and setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFeaturedArtists() {
        return featuredArtists;
    }

    public void setFeaturedArtists(String featuredArtists) {
        this.featuredArtists = featuredArtists;
    }

    public int getDuration() {
        return duration;
    }

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
}
