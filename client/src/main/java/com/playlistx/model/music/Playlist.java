package com.playlistx.model.music;

import java.util.Date;
import java.util.List;

public class Playlist {
    private int id;
    private String title;
    private String owner;
    private List<String> collaborators;
    private Date creationDate;
    private int songsCount;
    private boolean isPublic;
    private List<Song> songs;

    // Constructor
    public Playlist(int id, String title, String owner, List<String> collaborators, Date creationDate, int songsCount, boolean isPublic) {
        this.id = this.id;
        this.title = title;
        this.owner = owner;
        this.collaborators = this.collaborators;
        this.creationDate = this.creationDate;
        this.songsCount = this.songsCount;
        this.isPublic = this.isPublic;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<String> collaborators) {
        this.collaborators = collaborators;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getSongsCount() {
        return songsCount;
    }

    public void setSongsCount(int songsCount) {
        this.songsCount = songsCount;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }


    public void addCollaborator(String collaborator) {
        collaborators.add(collaborator);
    }


    public void removeCollaborator(String collaborator) {
        collaborators.remove(collaborator);
    }

    public void setDescription(String description) {
        return ;
    }


    public String getDescription() {
        // Implementation
        return "";
    }

    public void addSong(Song song) {
            this.songsCount++;
    }

    public void removeSong(Song song) {
        this.songsCount--;
    }

    public List<Song> getSongs() {

        return songs;
    }

    private List<Song> getAllSongs() {
        return getAllSongs();
    }

    public boolean isEmpty() {
        return songs.isEmpty();
    }

    // Method to get the total duration of the playlist
    public int getTotalDuration() {
        int totalDuration = 0;
        for (Song song : songs) {
            totalDuration += song.getDuration();
        }
        return totalDuration;
    }

    // Method to clear all songs from the playlist
    public void clear() {
        songs.clear();
    }


}
