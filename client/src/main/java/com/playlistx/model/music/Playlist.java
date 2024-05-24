package com.playlistx.model.music;

import java.util.Date;
import java.util.List;
import com.playlistx.model.database.SongDAO;
import com.playlistx.model.database.PlaylistDAO;
import com.playlistx.model.database.UserDAO;

public class Playlist {
    private int id;
    private String title;
    private int ownerid;
    private List<String> collaborators;
    private Date creationDate;
    private int songsCount;
    private boolean isPublic;
    private List<Song> songs;
    private SongDAO songDAO;
    private PlaylistDAO playlistDAO;
    private UserDAO userDAO;


    public Playlist(int id, SongDAO songDAO, String title, int owner, Date creationDate, int songsCount, boolean isPublic, List<String> collaborators, UserDAO userDAO) {
        this.id = id;
        this.songDAO = songDAO;
        this.title = title;
        this.ownerid = owner;
        this.creationDate = creationDate;
        this.songsCount = songsCount;
        this.isPublic = isPublic;
        this.collaborators = collaborators;
        this.userDAO = userDAO;
    }

    public Playlist(int id, SongDAO songDAO, PlaylistDAO playlistDAO, String title, int ownerid, java.util.Date creationDate, int songsCount, boolean isPublic) {
        this.id = id;
        this.songDAO = songDAO;
        this.playlistDAO = playlistDAO;
        this.title = title;
        this.ownerid = ownerid; // No need to parse as integer
        this.creationDate = creationDate;
        this.songsCount = songsCount;
        this.isPublic = isPublic;
    }

    public int getId() {
        return playlistDAO.getPlaylistIdByTitle(title);
    }


    public String getTitle() {
        if (title == null) {
            title = playlistDAO.getPlaylistTitleById(id);
            if (title == null) {
                title = "No Title";
            }
        }
        return title;
    }


    public String getOwner() {
        if (this.userDAO == null) {
            this.userDAO = new UserDAO();
        }
        return userDAO.getOwnerNameById(ownerid); // pass ownerid as int
    }

    public List<String> getCollaborators() {
        return playlistDAO.getCollaboratorsByPlaylistId(id);
    }

    public Date getCreationDate() {
        return playlistDAO.getCreationDateByPlaylistId(id);
    }

    public int getSongsCount() {
        return playlistDAO.getSongsCountByPlaylistId(id);
    }

    public boolean isPublic() {
        return playlistDAO.isPlaylistPublic(id);
    }

    public List<Song> getSongs() {
        return playlistDAO.getSongsByPlaylistId(id);
    }

    public void setOwner(int owner) {
        this.ownerid = owner;
        PlaylistDAO.updateOwnerByPlaylistId(id, owner);
    }

    public void setCollaborators(List<String> collaborators) {
        this.collaborators = collaborators;
        playlistDAO.updateCollaboratorsByPlaylistId(id, collaborators);
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        playlistDAO.updateCreationDateByPlaylistId(id, creationDate);
    }

    public void setSongsCount(int songsCount) {
        this.songsCount = songsCount;
        playlistDAO.updateSongsCountByPlaylistId(id, songsCount);
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
        playlistDAO.updateIsPublicByPlaylistId(id, isPublic);
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        playlistDAO.updateSongsByPlaylistId(id, songs);
    }

    public int getTotalDuration() {
        return songs.stream().mapToInt(Song::getDuration).sum();
    }

    public void addSong(Song song) {
        songs.add(song);
        playlistDAO.addSongToPlaylist(this.id, song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
        playlistDAO.removeSongFromPlaylist(this.id, song);
    }

    public void addCollaborator(String collaborator) {
        this.collaborators.add(collaborator);
        playlistDAO.updateCollaboratorsByPlaylistId(id, collaborators);
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
        playlistDAO.updateTitleByPlaylistId(id, newTitle);
    }
}