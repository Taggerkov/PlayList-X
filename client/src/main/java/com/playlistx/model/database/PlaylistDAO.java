package com.playlistx.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import com.playlistx.model.music.Song;
import com.playlistx.model.music.Playlist;
import java.util.ArrayList;
import java.sql.Date;

public class PlaylistDAO {
    private static DatabaseConnector dbConnector;

    public PlaylistDAO() {
        this.dbConnector = new DatabaseConnector();
    }

    public static void updateOwnerByPlaylistId(int id, int owner) {
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE Playlist SET ownerId = ? WHERE id = ?");
            statement.setString(1, String.valueOf(owner));
            statement.setInt(2, id);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPlaylistNameById(int playlistId) {
        String playlistName = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT title FROM playlist WHERE id = ?");
            statement.setInt(1, playlistId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                playlistName = resultSet.getString("title");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playlistName;
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



    public void addSongToPlaylist(int playlistId, com.playlistx.model.music.Song song) {
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

    public void removeSongFromPlaylist(int playlistId, com.playlistx.model.music.Song song) {
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

    public int getSongsCount(int playlistId) {
        int songsCount = 0;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM songList WHERE playlist_id = ?");
            statement.setInt(1, playlistId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                songsCount = resultSet.getInt(1);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songsCount;
    }

    public java.util.List<com.playlistx.model.music.Playlist> getAllPlaylists() {
        java.util.List<com.playlistx.model.music.Playlist> playlists = new java.util.ArrayList<>();
        try {
            java.sql.Connection connection = dbConnector.getConnection();
            java.sql.PreparedStatement statement = connection.prepareStatement("SELECT * FROM playlist");
            java.sql.ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                com.playlistx.model.music.Playlist playlist = new com.playlistx.model.music.Playlist(
                        resultSet.getInt("id"),
                        new SongDAO(),
                        this,
                        resultSet.getString("title"),
                        resultSet.getInt("ownerId"),
                        resultSet.getDate("creationDate"),
                        getSongsCount(resultSet.getInt("id")),
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

    public List<Playlist> getTopLikedPlaylistsByGenre(String genre, int limit) {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT p.*, COUNT(l.song_id) as likes_count " +
                "FROM playlist p " +
                "JOIN songList sl ON p.id = sl.playlist_id " +
                "JOIN song s ON sl.song_id = s.id " +
                "LEFT JOIN likes l ON s.id = l.song_id " +
                "WHERE s.genre = ? " +
                "GROUP BY p.id " +
                "ORDER BY likes_count DESC " +
                "LIMIT ?";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, genre);
            statement.setInt(2, limit);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Playlist playlist = new Playlist(
                        rs.getInt("id"),
                        new com.playlistx.model.database.SongDAO(),
                        this,
                        rs.getString("title"),
                        rs.getInt("ownerId"),
                        rs.getDate("creationDate"),
                        getSongsCount(rs.getInt("id")),
                        rs.getBoolean("isPublic")
                );
                playlists.add(playlist);
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }

        return playlists;
    }

    public String getPlaylistTitleById(int id) {
        String title = null;
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT title FROM playlist WHERE id = ?");
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


        public List<String> getCollaboratorsByPlaylistId(int id) {
            List<String> collaborators = new ArrayList<>();
            try (Connection connection = dbConnector.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT listener_id FROM collaborator WHERE playlist_id = ?");
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String collaborator = resultSet.getString("listener_id");
                        collaborators.add(collaborator);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return collaborators;
        }

        public Date getCreationDateByPlaylistId ( int id){
            Date creationDate = null;
            try (Connection connection = dbConnector.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT creationDate FROM playlist WHERE id = ?");
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        creationDate = resultSet.getDate("creationDate");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return creationDate;
        }

        public int getSongsCountByPlaylistId ( int id){
            int songsCount = 0;
            try (Connection connection = dbConnector.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM songList WHERE playlist_id = ?");
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        songsCount = resultSet.getInt(1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return songsCount;
        }

        public boolean isPlaylistPublic ( int id){
            boolean isPublic = false;
            try (Connection connection = dbConnector.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT isPublic FROM Playlist WHERE id = ?");
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        isPublic = resultSet.getBoolean("isPublic");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return isPublic;
        }

        public int getPlaylistIdByTitle (String title){
            int playlistId = -1;
            try (Connection connection = dbConnector.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT id FROM playlist WHERE title = ?");
                statement.setString(1, title);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        playlistId = resultSet.getInt("id");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return playlistId;
        }

    public List<Song> getSongsByPlaylistId(int id) {
        List<Song> songs = new ArrayList<>();
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT song.* FROM songList JOIN song ON songList.song_id = song.id WHERE songList.playlist_id = ?");
            statement.setInt(1, id);
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

    public void updateCollaboratorsByPlaylistId(int id, java.util.List<String> collaborators) {
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM collaborator WHERE playlist_id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            for (String collaborator : collaborators) {
                statement = connection.prepareStatement("INSERT INTO collaborator (playlist_id, listener_id) VALUES (?, ?)");
                statement.setInt(1, id);
                statement.setString(2, collaborator);
                statement.executeUpdate();
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCreationDateByPlaylistId(int id, java.util.Date creationDate) {
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE playlist SET creationDate = ? WHERE id = ?");
            statement.setDate(1, new java.sql.Date(creationDate.getTime()));
            statement.setInt(2, id);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSongsCountByPlaylistId(int id, int songsCount) {
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE playlist SET songsCount = ? WHERE id = ?");
            statement.setInt(1, songsCount);
            statement.setInt(2, id);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateIsPublicByPlaylistId(int id, boolean isPublic) {
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE playlist SET isPublic = ? WHERE id = ?");
            statement.setBoolean(1, isPublic);
            statement.setInt(2, id);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSongsByPlaylistId(int id, java.util.List<com.playlistx.model.music.Song> songs) {
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM songList WHERE playlist_id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            for (Song song : songs) {
                statement = connection.prepareStatement("INSERT INTO songList (playlist_id, song_id) VALUES (?, ?)");
                statement.setInt(1, id);
                statement.setInt(2, song.getId());
                statement.executeUpdate();
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateTitleByPlaylistId(int id, String newTitle) {
        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE playlist SET title = ? WHERE id = ?");
            statement.setString(1, newTitle);
            statement.setInt(2, id);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
