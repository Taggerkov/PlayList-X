package com.playlistx.model.music;

import java.util.List;
import com.playlistx.model.database.SongDAO;

public class Song {
    private int id;
    private SongDAO songDAO;
    private String artist;
    private int year;
    private String genre;
    private String title;
    private String albumName;
    private String link;
    private String featuredArtist;
    public Song(int id, String artist, int year, String genre, String title, String albumName, String link, String featuredArtist) {
        this.id = id;

        this.artist = artist;
        this.year = year;
        this.genre = genre;
        this.title = title;
        this.albumName = albumName;
        this.link = link;
        this.featuredArtist = featuredArtist;
        this.songDAO = new SongDAO();
    }


    public String getArtist() {
        return songDAO.getArtistById(id);
    }

    public int getYear() {
        return songDAO.getYearById(id);
    }

    public String getGenre() {
        return songDAO.getGenreById(id);
    }

    public String getTitle() {
        return songDAO.getTitleById(id);
    }

    public String getAlbumName() {
        return songDAO.getAlbumNameById(id);
    }

    public String getLink() {
        return songDAO.getLinkById(id);
    }

    public String getFeaturedArtists() {
        return songDAO.getFeaturedArtistsById(id);
    }

    public int getDuration() {
        return songDAO.getDurationById(id);
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", artist='" + getArtist() + '\'' +
                ", year=" + getYear() +
                ", genre='" + getGenre() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", albumName='" + getAlbumName() + '\'' +
                ", link='" + getLink() + '\'' +
                ", duration=" + getDuration() + " seconds" +
                ", featuredArtists='" + getFeaturedArtists() + '\'' +
                '}';
    }

    public String getFeaturedArtist() {
        return songDAO.getFeaturedArtistsById(id);
    }

    public int getId() {
        return id;
    }
}