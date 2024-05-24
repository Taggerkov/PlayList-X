package com.playlistx.model.music;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {
    private DatabaseConnector dbConnector;

    public SongDAO() {
        this.dbConnector = new DatabaseConnector();
    }

    public List<Song> getSongsFromPlaylist(int playlistId) {
        List<Song> songs = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM songs WHERE playlist_id =?");
            statement.setInt(1, playlistId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Song song = new Song(
                        resultSet.getInt("id"),
                        resultSet.getString("artist"),
                        resultSet.getInt("year"),
                        resultSet.getString("genre"),
                        resultSet.getString("title"),
                        resultSet.getString("albumName"),
                        resultSet.getString("link"),
                        resultSet.getString("featuredArtists")
                );
                songs.add(song);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    public int getSongsCount(int playlistId) {
        int count = 0;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM songs WHERE playlist_id = ?");
            statement.setInt(1, playlistId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<String> getCollaborators(int playlistId) {
        List<String> collaborators = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT username FROM collaborators WHERE playlist_id = ?");
            statement.setInt(1, playlistId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                collaborators.add(resultSet.getString("username"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collaborators;
    }

    public Playlist getPlaylistById(int playlistId) {
        Playlist playlist = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM playlists WHERE id = ?");
            statement.setInt(1, playlistId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                playlist = new Playlist(
                        resultSet.getInt("id"),
                        this,
                        resultSet.getString("title"),
                        resultSet.getString("owner"),
                        // You need to implement getCollaborators method
                        getCollaborators(playlistId),
                        resultSet.getDate("creationDate"),
                        // You need to implement getSongsCount method
                        getSongsCount(playlistId),
                        resultSet.getBoolean("isPublic")
                );
                // You need to implement getSongsFromPlaylist method
                playlist.setSongs(getSongsFromPlaylist(playlistId));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playlist;
    }

    public List<Song> getAllSongsFromPlaylist(int playlistId) {
        List<Song> songs = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM songs WHERE playlist_id = ?");
            statement.setInt(1, playlistId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Song song = new Song(resultSet.getInt("id"), resultSet.getString("artist"), resultSet.getInt("year"), resultSet.getString("genre"), resultSet.getString("title"), resultSet.getString("albumName"), resultSet.getString("link"), resultSet.getString("featuredArtists"));
                songs.add(song);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    public void addSongToPlaylist(int playlistId, Song song) {
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO songs (playlist_id, artist, year, genre, title, albumName, link, featuredArtists) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, playlistId);
            statement.setString(2, song.getArtist());
            statement.setInt(3, song.getYear());
            statement.setString(4, song.getGenre());
            statement.setString(5, song.getTitle());
            statement.setString(6, song.getAlbumName());
            statement.setString(7, song.getLink());
            statement.setString(8, song.getFeaturedArtists());
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeSongFromPlaylist(int playlistId, Song song) {
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM songs WHERE playlist_id = ? AND id = ?");
            statement.setInt(1, playlistId);
            statement.setInt(2, song.getId());
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Song> getMostLikedSongs() {
        List<Song> songs = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM songs ORDER BY likes DESC");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Song song = new Song(resultSet.getInt("id"), resultSet.getString("artist"), resultSet.getInt("year"), resultSet.getString("genre"), resultSet.getString("title"), resultSet.getString("albumName"), resultSet.getString("link"), resultSet.getString("featuredArtists"));
                songs.add(song);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    public int getPlaylistId(String playlistName) {
        int playlistId = -1;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM playlists WHERE name = ?");
            statement.setString(1, playlistName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                playlistId = resultSet.getInt("id");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playlistId;
    }

    public List<SongList> getSongList() {
        List<SongList> songLists = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT s.title as song_title, p.name as playlist_name FROM songList sl JOIN songs s ON sl.song_id = s.id JOIN playlists p ON sl.playlist_id = p.id");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SongList songList = new SongList(resultSet.getString("song_title"), resultSet.getString("playlist_name"), this);
                songLists.add(songList);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songLists;
    }


    public int getSongId(String songTitle) {
        int songId = -1;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM songs WHERE title = ?");
            statement.setString(1, songTitle);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                songId = resultSet.getInt("id");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songId;
    }

    public Song getSongByTitle(String songTitle) {
        Song song = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM songs WHERE title = ?");
            statement.setString(1, songTitle);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                song = new Song(
                        resultSet.getInt("id"),
                        resultSet.getString("artist"),
                        resultSet.getInt("year"),
                        resultSet.getString("genre"),
                        resultSet.getString("title"),
                        resultSet.getString("albumName"),
                        resultSet.getString("link"),
                        resultSet.getString("featuredArtists")
                );
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return song;
    }

    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM songs");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Song song = new Song(
                        resultSet.getInt("id"),
                        resultSet.getString("artist"),
                        resultSet.getInt("year"),
                        resultSet.getString("genre"),
                        resultSet.getString("title"),
                        resultSet.getString("albumName"),
                        resultSet.getString("link"),
                        resultSet.getString("featuredArtists")
                );
                songs.add(song);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

}