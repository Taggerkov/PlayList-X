package com.playlistx.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.playlistx.model.music.Song;
import com.playlistx.model.music.SongList;
import com.playlistx.model.music.Playlist;



public class SongDAO {
    private DatabaseConnector dbConnector;

    public SongDAO() {
        this.dbConnector = new DatabaseConnector();
    }

    public List<Song> getSongsFromPlaylist(int playlistId) {
        List<Song> songs = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT song.* FROM song " +
                            "JOIN songList ON song.id = songList.song_id " +
                            "WHERE songList.playlist_id = ?"
            );
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
                        resultSet.getString("featuredartist")
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
            // Replace "playlist_id" with the correct column name
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM playlist WHERE Songscount = ?");
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
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT Listener.username FROM collaborator " +
                            "JOIN Listener ON collaborator.listener_id = Listener.id " +
                            "WHERE collaborator.playlist_id = ?"
            );
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



    public List<Song> getAllSongsFromPlaylist(int playlistId) {
        List<Song> songs = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT song.* FROM song " +
                            "JOIN songList ON song.id = songList.song_id " +
                            "WHERE songList.playlist_id = ?"
            );
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
                        resultSet.getString("featuredArtist") // corrected from "featuredArtists" to "featuredArtist"
                );
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
            // Insert the song into the song table
            PreparedStatement statement = connection.prepareStatement("INSERT INTO song (artist, year, genre, title, albumName, link, featuredArtist) VALUES (?, ?, ?, ?, ?, ?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, song.getArtist());
            statement.setInt(2, song.getYear());
            statement.setString(3, song.getGenre());
            statement.setString(4, song.getTitle());
            statement.setString(5, song.getAlbumName());
            statement.setString(6, song.getLink());
            statement.setString(7, song.getFeaturedArtist());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new java.sql.SQLException("Creating song failed, no rows affected.");
            }

            // Retrieve the ID of the inserted song
            int songId;
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    songId = generatedKeys.getInt(1);
                } else {
                    throw new java.sql.SQLException("Creating song failed, no ID obtained.");
                }
            }

            // Insert the song and playlist relationship into the songList table
            statement = connection.prepareStatement("INSERT INTO songList (playlist_id, song_id) VALUES (?, ?)");
            statement.setInt(1, playlistId);
            statement.setInt(2, songId);
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removeSongFromPlaylist(int playlistId, Song song) {
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM songList WHERE playlist_id = ? AND song_id = ?");
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
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT song.*, COUNT(likes.song_id) as likes_count " +
                            "FROM song LEFT JOIN likes ON song.id = likes.song_id " +
                            "GROUP BY song.id " +
                            "ORDER BY likes_count DESC"
            );
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
                        resultSet.getString("featuredArtist") // corrected from "featuredArtists" to "featuredArtist"
                );
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
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM playlist WHERE title = ?");
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
            PreparedStatement statement = connection.prepareStatement("SELECT s.title as song_title, p.name as playlist_name FROM songList sl JOIN song s ON sl.song_id = s.id JOIN playlist p ON sl.playlist_id = p.id");
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
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM song WHERE title = ?");
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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM song WHERE title = ?");
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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"song\""); // changed "song" to "Song"
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
                        resultSet.getString("featuredartist")
                );
                songs.add(song);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    public List<String> getAllGenres() {
        List<String> genres = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT genre FROM song");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                genres.add(resultSet.getString("genre"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genres;
    }

    public List<Song> getTopLikedSongsByGenre(String genre, int limit) {
        List<Song> songs = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT song.*, COUNT(likes.song_id) as likes_count " +
                            "FROM song LEFT JOIN likes ON song.id = likes.song_id " +
                            "WHERE song.genre = ? " +
                            "GROUP BY song.id " +
                            "ORDER BY likes_count DESC " +
                            "LIMIT ?"
            );
            statement.setString(1, genre);
            statement.setInt(2, limit);
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
                        resultSet.getString("featuredArtist") // corrected from "featuredArtists" to "featuredArtist"
                );
                songs.add(song);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }
    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"playlist\"");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Playlist playlist = new Playlist(
                        resultSet.getInt("id"),
                        this,
                        resultSet.getString("title"),
                        resultSet.getString("ownerid"), // replace with the correct column name
                        resultSet.getDate("creationDate"),
                        getSongsCount(resultSet.getInt("id")), // ensure getSongsCount method uses correct table name
                        resultSet.getBoolean("isPublic")
                );
                playlists.add(playlist);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playlists;
    }



}