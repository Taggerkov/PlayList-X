package com.playlistx.model.music;

import java.util.Date;
import java.util.List;
import com.playlistx.model.music.Song;
import com.playlistx.model.login.User;

/**
 * Represents a playlist in the music system. It includes metadata about the playlist
 * such as its title, owner, collaborators, and the list of songs it contains.
 */
public class Playlist {
    private int id;
    private String title;
    private String ownerid;
    private List<String> collaborators;
    private Date creationDate;
    private int songsCount;
    private boolean isPublic;
    private List<Song> songs;
    private SongDAO songDAO; // Add a SongDAO to interact with the database

    /**
     * Constructs a Playlist instance.
     *
     * @param id the unique identifier for the playlist
     * @param title the title of the playlist
     * @param ownerid the owner of the playlist
     * @param creationDate the date the playlist was created
     * @param songsCount the count of songs currently in the playlist
     * @param isPublic a flag indicating if the playlist is public or private
     */
    public Playlist(int id, SongDAO songDAO, String title, String ownerid, Date creationDate, int songsCount, boolean isPublic) {
        this.id = id;
        this.songDAO = songDAO;
        this.title = title;
        this.ownerid = ownerid;
        this.creationDate = creationDate;
        this.songsCount = songsCount;
        this.isPublic = isPublic;
        this.songs = songDAO.getSongsFromPlaylist(id); // Fetch the songs from the database
    }

    public void share(){

    }

    public void unshare(){

    }

    // Standard getters and setters for each property

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
        return ownerid;
    }

    public void setOwner(String owner) {
        this.ownerid = owner;
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



    public void setSongsCount(int songsCount) {
        this.songsCount = songsCount;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    // Functionalities related to playlist modification

    /**
     * Adds a collaborator to the playlist.
     *
     * @param collaborator the collaborator to add
     */
    public void addCollaborator(String collaborator) {
        collaborators.add(collaborator);
    }

    /**
     * Removes a collaborator from the playlist.
     *
     * @param collaborator the collaborator to remove
     */
    public void removeCollaborator(String collaborator) {
        collaborators.remove(collaborator);
    }







    /**
     * Checks if the playlist is empty.
     *
     * @return true if there are no songs in the playlist, otherwise false
     */
    public boolean isEmpty() {
        return songs.isEmpty();
    }



    /**
     * Clears all songs from the playlist and resets the song count.
     */
    public void clear() {
        songs.clear();
        songsCount = 0;
    }
    public List<Song> getSongs() {
        return songs;
    }



    public int getSongsCount() {
        return songs.size();
    }

    public int getTotalDuration() {
        return songs.stream().mapToInt(Song::getDuration).sum();
    }

    public void addSong(Song song) {
        songs.add(song);
        songDAO.addSongToPlaylist(this.id, song); // Add the song to the playlist in the database
    }

    public void removeSong(Song song) {
        songs.remove(song);
        songDAO.removeSongFromPlaylist(this.id, song); // Remove the song from the playlist in the database
    }

    public void setSongs(java.util.List<com.playlistx.model.music.Song> songsFromPlaylist) {
        this.songs = songsFromPlaylist;
    }
}

