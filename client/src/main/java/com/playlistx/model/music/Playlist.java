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
    private String owner;
    private List<String> collaborators;
    private Date creationDate;
    private int songsCount;
    private boolean isPublic;
    private List<Song> songs;

    /**
     * Constructs a Playlist instance.
     *
     * @param id the unique identifier for the playlist
     * @param title the title of the playlist
     * @param owner the owner of the playlist
     * @param collaborators a list of collaborators who have access to the playlist
     * @param creationDate the date the playlist was created
     * @param songsCount the count of songs currently in the playlist
     * @param isPublic a flag indicating if the playlist is public or private
     */
    public Playlist(int id, String title, String owner, List<String> collaborators, Date creationDate, int songsCount, boolean isPublic) {
        this.id = id;
        this.title = title;
        this.owner = owner;
        this.collaborators = collaborators;
        this.creationDate = creationDate;
        this.songsCount = songsCount;
        this.isPublic = isPublic;
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
     * Adds a song to the playlist and increments the song count.
     *
     * @param song the song to add to the playlist
     */
    public void addSong(Song song) {
        songs.add(song);
        this.songsCount++;
    }

    /**
     * Removes a song from the playlist and decrements the song count.
     *
     * @param song the song to remove from the playlist
     */
    public void removeSong(Song song) {
        songs.remove(song);
        this.songsCount--;
    }

    /**
     * Retrieves all songs in the playlist.
     *
     * @return a list of all songs in the playlist
     */
    public List<Song> getSongs() {
        return songs;
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
     * Calculates the total duration of all songs in the playlist.
     *
     * @return the total duration of all songs in seconds
     */
    public int getTotalDuration() {
        int totalDuration = 0;
        for (Song song : songs) {
            totalDuration += song.getDuration();
        }
        return totalDuration;
    }

    /**
     * Clears all songs from the playlist and resets the song count.
     */
    public void clear() {
        songs.clear();
        songsCount = 0;
    }
}
