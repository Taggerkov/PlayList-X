package com.playlistx.model.music;
import java.util.List;
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
    private SongDAO songDAO;

    public Song(String songTitle, SongDAO songDAO) {
        this.songDAO = songDAO;
        Song song = songDAO.getSongByTitle(songTitle);
        this.id = song.getId();
        this.artist = song.getArtist();
        this.year = song.getYear();
        this.genre = song.getGenre();
        this.title = song.getTitle();
        this.albumName = song.getAlbumName();
        this.link = song.getLink();
        this.featuredArtists = song.getFeaturedArtists();
    }

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

    public List<Song> getAllSongs() {
        return this.songs;
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

    public String getFeaturedArtist() {
        return featuredArtists;
    }
}
