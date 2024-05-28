package com.playlistx.model.proxy;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import com.playlistx.model.music.Song;
import com.playlistx.model.database.SongDAO;
import com.playlistx.model.music.Playlist;
import com.playlistx.model.music.SongList;

public class SongServiceImpl extends UnicastRemoteObject implements SongService {
    private SongDAO songDAO;

    public SongServiceImpl() throws RemoteException {
        songDAO = new SongDAO();
    }

    @Override
    public List<Song> getAllSongs() throws RemoteException {
        return songDAO.getAllSongs();
    }

    @Override
    public List<Song> getSongsFromPlaylist(int playlistId) throws RemoteException {
        return songDAO.getSongsFromPlaylist(playlistId);
    }

    @Override
    public int getSongsCount(int playlistId) throws RemoteException {
        return songDAO.getSongsCount(playlistId);
    }

    @Override
    public List<String> getCollaborators(int playlistId) throws RemoteException {
        return songDAO.getCollaborators(playlistId);
    }

    @Override
    public List<Song> getAllSongsFromPlaylist(int playlistId) throws RemoteException {
        return songDAO.getAllSongsFromPlaylist(playlistId);
    }

    @Override
    public void addSongToPlaylist(int playlistId, Song song) throws RemoteException {
        songDAO.addSongToPlaylist(playlistId, song);
    }

    @Override
    public void removeSongFromPlaylist(int playlistId, Song song) throws RemoteException {
        songDAO.removeSongFromPlaylist(playlistId, song);
    }

    @Override
    public List<Song> getMostLikedSongs() throws RemoteException {
        return songDAO.getMostLikedSongs();
    }

    @Override
    public int getPlaylistId(String playlistName) throws RemoteException {
        return songDAO.getPlaylistId(playlistName);
    }

    @Override
    public List<SongList> getSongList() throws RemoteException {
        return songDAO.getSongList();
    }

    @Override
    public int getSongId(String songTitle) throws RemoteException {
        return songDAO.getSongId(songTitle);
    }

    @Override
    public Song getSongByTitle(String songTitle) throws RemoteException {
        return songDAO.getSongByTitle(songTitle);
    }

    @Override
    public List<String> getAllGenres() throws RemoteException {
        return songDAO.getAllGenres();
    }

    @Override
    public List<Song> getTopLikedSongsByGenre(String genre, int limit) throws RemoteException {
        return songDAO.getTopLikedSongsByGenre(genre, limit);
    }

    @Override
    public List<Playlist> getAllPlaylists() throws RemoteException {
        return songDAO.getAllPlaylists();
    }
}