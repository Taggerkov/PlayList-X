package com.playlistx.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.playlistx.model.music.Song;
import com.playlistx.model.music.Listener;

public class SongDAO {
    private com.playlistx.model.database.DatabaseConnector dbConnector;

    public SongDAO() {
        this.dbConnector = new DatabaseConnector();
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
                com.playlistx.model.music.Song song = new com.playlistx.model.music.Song(
                        resultSet.getInt("id"),
                        resultSet.getString("artist"),
                        resultSet.getInt("year"),
                        resultSet.getString("genre"),
                        resultSet.getString("title"),
                        resultSet.getString("albumName"),
                        resultSet.getString("link"),
                        resultSet.getString("featuredArtist")
                );
                songs.add(song);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }


    public List<com.playlistx.model.music.SongList> getSongList() {
        List<com.playlistx.model.music.SongList> songLists = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT s.title as song_title, p.name as playlist_name FROM songList sl JOIN song s ON sl.song_id = s.id JOIN playlist p ON sl.playlist_id = p.id");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                com.playlistx.model.music.SongList songList = new com.playlistx.model.music.SongList(resultSet.getString("song_title"), resultSet.getString("playlist_name"), this);
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
                song = new com.playlistx.model.music.Song(
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

    public List<com.playlistx.model.music.Song> getAllSongs() {
        List<com.playlistx.model.music.Song> songs = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"song\""); // changed "song" to "Song"
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                com.playlistx.model.music.Song song = new com.playlistx.model.music.Song(
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

    public List<com.playlistx.model.music.Song> getTopLikedSongsByGenre(String genre, int limit) {
        List<com.playlistx.model.music.Song> songs = new ArrayList<>();
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
                com.playlistx.model.music.Song song = new com.playlistx.model.music.Song(
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


    public com.playlistx.model.music.Listener getListenerById(int id) {
        com.playlistx.model.music.Listener listener = null;
        String sql = "SELECT * FROM Listener WHERE id = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                boolean isAdmin = rs.getBoolean("isAdmin");
                byte[] hash = rs.getBytes("hash");
                byte[] salt = rs.getBytes("salt");

                listener = new Listener(id, username, isAdmin, hash, salt);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return listener;
    }


    public String getArtistById(int id) {
        String artist = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT artist FROM song WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                artist = resultSet.getString("artist");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return artist;
    }

    public int getYearById(int id) {
        int year = 0;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT year FROM song WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                year = resultSet.getInt("year");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return year;
    }

    public String getGenreById(int id) {
        String genre = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT genre FROM song WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                genre = resultSet.getString("genre");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genre;
    }

    public String getTitleById(int id) {
        String title = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT title FROM song WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                title = resultSet.getString("title");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return title;
    }

    public String getAlbumNameById(int id) {
        String albumName = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT albumName FROM song WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                albumName = resultSet.getString("albumName");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return albumName;
    }

    public String getLinkById(int id) {
        String link = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT link FROM song WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                link = resultSet.getString("link");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return link;
    }

    public String getFeaturedArtistsById(int id) {
        String featuredArtists = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT featuredArtist FROM song WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                featuredArtists = resultSet.getString("featuredArtist");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return featuredArtists;
    }

    public int getDurationById(int id) {
        int duration = 0;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT duration FROM song WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                duration = resultSet.getInt("duration");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duration;
    }

    public int getSongIdBySongListId(int songListId) {
        int songId = -1;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT song_id FROM songList WHERE song_id = ?");
            statement.setInt(1, songListId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                songId = resultSet.getInt("song_id");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songId;
    }

    public int getPlaylistIdBySongListId(int songListId) {
        int playlistId = -1;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT playlist_id FROM songList WHERE song_id = ?");
            statement.setInt(1, songListId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                playlistId = resultSet.getInt("playlist_id");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playlistId;
    }

    public java.util.List<com.playlistx.model.music.Song> getSongsFromPlaylist(int playlistId) {
        List<com.playlistx.model.music.Song> songs = new ArrayList<>();
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
                com.playlistx.model.music.Song song = new com.playlistx.model.music.Song(
                        resultSet.getInt("id"),
                        resultSet.getString("artist"),
                        resultSet.getInt("year"),
                        resultSet.getString("genre"),
                        resultSet.getString("title"),
                        resultSet.getString("albumName"),
                        resultSet.getString("link"),
                        resultSet.getString("featuredArtist")
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



   