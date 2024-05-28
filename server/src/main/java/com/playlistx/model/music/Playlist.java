package com.playlistx.model.music;
import com.playlistx.model.database.SongDAO;

/**
 * Represents a playlist in the music system. It includes metadata about the playlist
 * such as its title, owner, collaborators, and the list of songs it contains.
 */
public class Playlist {
    private int id;
    private String title;
    private String ownerid;
    private java.util.List<String> collaborators;
    private java.util.Date creationDate;
    private int songsCount;
    private boolean isPublic;
    private java.util.List<Song> songs;
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
    public Playlist(int id, SongDAO songDAO, String title, String ownerid, java.util.Date creationDate, int songsCount, boolean isPublic) {
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
/**
     * Gets the unique identifier for the playlist.
     *
     * @return the unique identifier
     */
    public int getId() {
        return id;
    }
    /**
     * Sets the unique identifier for the playlist.
     *
     * @param id the unique identifier
     */

    public void setId(int id) {
        this.id = id;
    }
    /**
     * Gets the title of the playlist.
     *
     * @return the title
     */

    public String getTitle() {
        return title;
    }
    /**
     * Sets the title of the playlist.
     *
     * @param title the title
     */

    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Gets the owner of the playlist.
     *
     * @return the owner
     */

    public String getOwner() {
        return ownerid;
    }
    /**
     * Sets the owner of the playlist.
     *
     * @param owner the owner
     */

    public void setOwner(String owner) {
        this.ownerid = owner;
    }
    /**
     * Gets the list of collaborators for the playlist.
     *
     * @return the list of collaborators
     */

    public java.util.List<String> getCollaborators() {
        return collaborators;
    }
    /**
     * Sets the list of collaborators for the playlist.
     *
     * @param collaborators the list of collaborators
     */

    public void setCollaborators(java.util.List<String> collaborators) {
        this.collaborators = collaborators;
    }
    /**
     * Gets the date the playlist was created.
     *
     * @return the creation date
     */

    public java.util.Date getCreationDate() {
        return creationDate;
    }
    /**
     * Sets the date the playlist was created.
     *
     * @param creationDate the creation date
     */

    public void setCreationDate(java.util.Date creationDate) {
        this.creationDate = creationDate;
    }
    /**
     * Gets the count of songs currently in the playlist.
     *
     * @return the count of songs
     */



    public void setSongsCount(int songsCount) {
        this.songsCount = songsCount;
    }
    /**
     * Checks if the playlist is public.
     *
     * @return true if the playlist is public, otherwise false
     */

    public boolean isPublic() {
        return isPublic;
    }
    /**
     * Sets the flag indicating if the playlist is public or private.
     *
     * @param isPublic a flag indicating if the playlist is public or private
     */

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
    /**
     * Gets the list of songs in the playlist.
     *
     * @return the list of songs
     */
    public java.util.List<Song> getSongs() {
        return songs;
    }
    /**
     * Gets the number of songs in the playlist.
     *
     * @return the number of songs
     */
    public int getSongsCount() {
        return songs.size();
    }

    /**
     * Gets the total duration of all songs in the playlist.
     *
     * @return the total duration in seconds
     */
    public int getTotalDuration() {
        return songs.stream().mapToInt(com.playlistx.model.music.Song::getDuration).sum();
    }

    /**
     * Adds a song to the playlist.
     *
     * @param song the song to add
     */
    public void addSong(com.playlistx.model.music.Song song) {
        songs.add(song);
        songDAO.addSongToPlaylist(this.id, song); // Add the song to the playlist in the database
    }
    /**
     * Removes a song from the playlist.
     *
     * @param song the song to remove
     */

    public void removeSong(com.playlistx.model.music.Song song) {
        songs.remove(song);
        songDAO.removeSongFromPlaylist(this.id, song); // Remove the song from the playlist in the database
    }

    /**
     * * Sets the list of songs in the playlist.
     * @param songsFromPlaylist
     */

    public void setSongs(java.util.List<com.playlistx.model.music.Song> songsFromPlaylist) {
        this.songs = songsFromPlaylist;
    }
}

