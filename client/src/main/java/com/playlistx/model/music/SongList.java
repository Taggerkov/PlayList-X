package com.playlistx.model.music;

/**
 * Represents an association between a song and a playlist.
 * This class links songs to playlists using their identifiers, facilitating many-to-many relationships
 * where songs can belong to multiple playlists and playlists can contain multiple songs.
 */
public class SongList {
    private int songId;  // The unique identifier for a song.
    private int playlistId;  // The unique identifier for a playlist.

    /**
     * Constructs a SongList object to represent the relationship between a song and a playlist.
     *
     * @param songId the ID of the song in the relationship.
     * @param playlistId the ID of the playlist in the relationship.
     */
    public SongList(int songId, int playlistId) {
        this.songId = songId;
        this.playlistId = playlistId;
    }

    /**
     * Gets the song ID.
     *
     * @return the ID of the song.
     */
    public int getSongId() {
        return songId;
    }

    /**
     * Sets the song ID.
     *
     * @param songId the new ID of the song.
     */
    public void setSongId(int songId) {
        this.songId = songId;
    }

    /**
     * Gets the playlist ID.
     *
     * @return the ID of the playlist.
     */
    public int getPlaylistId() {
        return playlistId;
    }

    /**
     * Sets the playlist ID.
     *
     * @param playlistId the new ID of the playlist.
     */
    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    /**
     * Returns a string representation of the SongList object.
     *
     * @return A string describing the song ID and playlist ID association.
     */
    @Override
    public String toString() {
        return "SongList{" +
                "songId=" + songId +
                ", playlistId=" + playlistId +
                '}';
    }
}
