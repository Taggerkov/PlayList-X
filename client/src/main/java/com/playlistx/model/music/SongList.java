package com.playlistx.model.music;



public class SongList {
    private int songId;
    private int playlistId;

    // Constructor
    public SongList(int songId, int playlistId) {
        this.songId = songId;
        this.playlistId = playlistId;
    }


    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    // toString method to represent the song list as a string
    @Override
    public String toString() {
        return "SongList{" +
                "songId=" + songId +
                ", playlistId=" + playlistId +
                '}';
    }
}

