package com.playlistx.model.proxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import com.playlistx.model.music.Song;
import com.playlistx.model.music.SongList;
import com.playlistx.model.music.Playlist;

public interface SongService extends Remote {
    List<Song> getAllSongs() throws RemoteException;
    List<Song> getSongsFromPlaylist(int playlistId) throws RemoteException;
    int getSongsCount(int playlistId) throws RemoteException;
    List<String> getCollaborators(int playlistId) throws RemoteException;
    List<Song> getAllSongsFromPlaylist(int playlistId) throws RemoteException;
    void addSongToPlaylist(int playlistId, Song song) throws RemoteException;
    void removeSongFromPlaylist(int playlistId, Song song) throws RemoteException;
    List<Song> getMostLikedSongs() throws RemoteException;
    int getPlaylistId(String playlistName) throws RemoteException;
    List<SongList> getSongList() throws RemoteException;
    int getSongId(String songTitle) throws RemoteException;
    Song getSongByTitle(String songTitle) throws RemoteException;
    List<String> getAllGenres() throws RemoteException;
    List<Song> getTopLikedSongsByGenre(String genre, int limit) throws RemoteException;
    List<Playlist> getAllPlaylists() throws RemoteException;
}