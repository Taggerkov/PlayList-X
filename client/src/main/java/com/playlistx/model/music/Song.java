package com.playlistx.model.music;

public class Song {
    private int id;
    private String artist;
    private int year;
    private String genre;
    private String title;
    private String albumName;
    private String link;
    private int duration;
    private String featuredArtists;

    // Constructor
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

    // Getters and setters
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

    // toString method to represent the song as a string
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
                ", featuredArtists='" + featuredArtists + '\'' +
                '}';
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
