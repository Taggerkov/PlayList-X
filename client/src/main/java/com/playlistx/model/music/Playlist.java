package com.playlistx.model.music;

import java.util.Date;
import java.util.List;
import com.playlistx.model.proxy.SongService;
import java.rmi.RemoteException;

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
    private SongService songService; // Service for interacting with the server

    /**
     * Constructs a Playlist instance.
     *
     * @param id           the unique identifier for the playlist
     * @param songService  the service used to fetch song and playlist details
     * @param title        the title of the playlist
     * @param ownerid      the owner of the playlist
     * @param creationDate the date the playlist was created
     * @param songsCount   the count of songs currently in the playlist
     * @param isPublic     a flag indicating if the playlist is public or private
     */
    public Playlist(int id, SongService songService, String title, String ownerid, Date creationDate, int songsCount, boolean isPublic) {
        this.id = id;
        this.songService = songService;
        this.title = title;
        this.ownerid = ownerid;
        this.creationDate = creationDate;
        this.songsCount = songsCount;
        this.isPublic = isPublic;
        try {
            this.songs = songService.getSongsFromPlaylist(id); // Fetch the songs from the server
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shares the playlist.
     */
    public void share() {
        // Implementation for sharing the playlist
    }

    /**
     * Unshares the playlist.
     */
    public void unshare() {
        // Implementation for unsharing the playlist
    }

    // Standard getters and setters for each property

    /**
     * Gets the unique identifier of the playlist.
     *
     * @return the ID of the playlist.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the playlist.
     *
     * @param id the new ID of the playlist.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the title of the playlist.
     *
     * @return the title of the playlist.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the playlist.
     *
     * @param title the new title of the playlist.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the owner ID of the playlist.
     *
     * @return the owner ID of the playlist.
     */
    public String getOwner() {
        return ownerid;
    }

    /**
     * Sets the owner ID of the playlist.
     *
     * @param owner the new owner ID of the playlist.
     */
    public void setOwner(String owner) {
        this.ownerid = owner;
    }

    /**
     * Gets the list of collaborators of the playlist.
     *
     * @return the list of collaborators.
     */
    public List<String> getCollaborators() {
        return collaborators;
    }

    /**
     * Sets the list of collaborators of the playlist.
     *
     * @param collaborators the new list of collaborators.
     */
    public void setCollaborators(List<String> collaborators) {
        this.collaborators = collaborators;
    }

    /**
     * Gets the creation date of the playlist.
     *
     * @return the creation date of the playlist.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date of the playlist.
     *
     * @param creationDate the new creation date.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Sets the song count of the playlist.
     *
     * @param songsCount the new song count.
     */
    public void setSongsCount(int songsCount) {
        this.songsCount = songsCount;
    }

    /**
     * Checks if the playlist is public.
     *
     * @return true if the playlist is public, false otherwise.
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Sets the visibility of the playlist.
     *
     * @param isPublic true if the playlist should be public, false otherwise.
     */
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    // Functionalities related to playlist modification

    /**
     * Adds a collaborator to the playlist.
     *
     * @param collaborator the collaborator to add.
     */
    public void addCollaborator(String collaborator) {
        collaborators.add(collaborator);
    }

    /**
     * Removes a collaborator from the playlist.
     *
     * @param collaborator the collaborator to remove.
     */
    public void removeCollaborator(String collaborator) {
        collaborators.remove(collaborator);
    }

    /**
     * Checks if the playlist is empty.
     *
     * @return true if there are no songs in the playlist, otherwise false.
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

    /**
     * Gets the list of songs in the playlist.
     *
     * @return the list of songs.
     */
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * Sets the songs in the playlist.
     *
     * @param songsFromPlaylist the new list of songs.
     */
    public void setSongs(List<Song> songsFromPlaylist) {
        this.songs = songsFromPlaylist;
    }

    /**
     * Returns the song count.
     *
     * @return the song count.
     */
    public int getSongsCount() {
        return songs.size();
    }

    /**
     * Returns the total duration of all songs in the playlist.
     *
     * @return the total duration.
     */
    public int getTotalDuration() {
        return songs.stream().mapToInt(Song::getDuration).sum();
    }

    /**
     * Adds a song to the playlist and updates the server.
     *
     * @param song the song to add.
     */
    public void addSong(Song song) {
        songs.add(song);
        try {
            songService.addSongToPlaylist(this.id, song); // Add the song to the playlist on the server
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a song from the playlist and updates the server.
     *
     * @param song the song to remove.
     */
    public void removeSong(Song song) {
        songs.remove(song);
        try {
            songService.removeSongFromPlaylist(this.id, song); // Remove the song from the playlist on the server
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
