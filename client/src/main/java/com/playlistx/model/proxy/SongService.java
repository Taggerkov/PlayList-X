package com.playlistx.model.proxy;

public interface SongService extends java.rmi.Remote {
    java.util.List<com.playlistx.model.music.Song> getAllSongs() throws java.rmi.RemoteException;
    java.util.List<com.playlistx.model.music.Song> getSongsFromPlaylist(int playlistId) throws java.rmi.RemoteException;
    int getSongsCount(int playlistId) throws java.rmi.RemoteException;
    java.util.List<String> getCollaborators(int playlistId) throws java.rmi.RemoteException;
    java.util.List<com.playlistx.model.music.Song> getAllSongsFromPlaylist(int playlistId) throws java.rmi.RemoteException;
    void addSongToPlaylist(int playlistId, com.playlistx.model.music.Song song) throws java.rmi.RemoteException;
    void removeSongFromPlaylist(int playlistId, com.playlistx.model.music.Song song) throws java.rmi.RemoteException;
    java.util.List<com.playlistx.model.music.Song> getMostLikedSongs() throws java.rmi.RemoteException;
    int getPlaylistId(String playlistName) throws java.rmi.RemoteException;
    java.util.List<com.playlistx.model.music.SongList> getSongList() throws java.rmi.RemoteException;
    int getSongId(String songTitle) throws java.rmi.RemoteException;
    com.playlistx.model.music.Song getSongByTitle(String songTitle) throws java.rmi.RemoteException;
    java.util.List<String> getAllGenres() throws java.rmi.RemoteException;
    java.util.List<com.playlistx.model.music.Song> getTopLikedSongsByGenre(String genre, int limit) throws java.rmi.RemoteException;
    java.util.List<com.playlistx.model.music.Playlist> getAllPlaylists() throws java.rmi.RemoteException;
}