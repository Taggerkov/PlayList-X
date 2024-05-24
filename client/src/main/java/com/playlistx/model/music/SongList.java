package com.playlistx.model.music;

public class SongList {
    private SongDAO songDAO;  // The SongDAO to interact with the database.
    private int songId;  // The unique identifier for a song.
    private int playlistId;  // The unique identifier for a playlist.

    /**
     * Constructs a SongList object to represent the relationship between a song and a playlist.
     *
     * @param songTitle    the title of the song in the relationship.
     * @param playlistName the name of the playlist in the relationship.
     */
    public SongList(String songTitle, String playlistName, com.playlistx.model.music.SongDAO songDAO) {
        this.songDAO = songDAO;
        this.songId = songDAO.getSongId(songTitle);
        this.playlistId = songDAO.getPlaylistId(playlistName);
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
     * Gets the playlist ID.
     *
     * @return the ID of the playlist.
     */
    public int getPlaylistId() {
        return playlistId;
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