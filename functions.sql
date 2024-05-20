CREATE OR  REPLACE FUNCTION update_song_count()
RETURNS TRIGGER AS
    $$
    BEGIN
        UPDATE Playlist
        SET songsCount = (SELECT count(song_id) FROM song, songList, Playlist WHERE Song.id=song_id and playlist_id=Playlist.id);
        RETURN NEW;
    end;
    $$
LANGUAGE plpgsql;

CREATE TRIGGER update_sngcnt_trigger
    AFTER INSERT ON songList
    FOR EACH ROW
    EXECUTE FUNCTION update_song_count();


CREATE OR  REPLACE FUNCTION update_total_duration()
RETURNS TRIGGER AS
    $$
    BEGIN
        UPDATE Playlist
        SET totalDuration = (SELECT sum(duration) FROM song, songList WHERE Song.id=song_id);
        RETURN NEW;
    end;
    $$
LANGUAGE plpgsql;

CREATE TRIGGER update_ttldrnt_trigger
    AFTER INSERT ON songList
    FOR EACH ROW
    EXECUTE FUNCTION update_total_duration();


CREATE  OR REPLACE  FUNCTION add_new_song(
    songID smallint,
    songTitle varchar,
    artistName varchar,
    yearNo int,
    albumTitle varchar,
    ftArtist varchar,
    songDuration int,
    genreName varchar,
    songLink varchar)
RETURNS TRIGGER AS
    $$
    BEGIN
        INSERT INTO song VALUES (songID, songTitle, artistName, yearNo, albumTitle, ftArtist, songDuration, genreName, songLink);
    end;
    $$
language plpgsql;

CREATE TRIGGER add_song_trigger
    BEFORE UPDATE ON song
    FOR EACH ROW
    EXECUTE FUNCTION add_new_song();

CREATE OR REPLACE FUNCTION get_songs(song_name TEXT)
RETURNS TABLE (
    id smallint,
    title varchar,
    artist varchar,
    year int,
    albumName varchar(50),
    featuredArtist varchar,
    duration int,
    genre varchar,
    link varchar
)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY EXECUTE 'SELECT * FROM ' || quote_ident(song_name);
END;
$$;

SELECT * FROM get_songs('song'); --Runs the function

DROP FUNCTION get_songs(text);


CREATE OR REPLACE FUNCTION get_playlists(user_id TEXT)
RETURNS TABLE (
    id smallint,
    title varchar,
    ownerId smallint,
    creationDate date,
    songsCount int,
    isPublic boolean,
    totalDuration int
)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY EXECUTE 'SELECT id, title, ownerId, DATE(creationDate), songsCount, isPublic, totalDuration FROM playlist WHERE ownerId = CAST($1 AS smallint)' USING user_id;
    RETURN QUERY EXECUTE 'SELECT id, title, ownerId, DATE(creationDate), songsCount, isPublic, totalDuration FROM playlist WHERE isPublic = true';
END;
$$;

SELECT DISTINCT * FROM get_playlists('1');



CREATE OR REPLACE FUNCTION add_like(u_id INT, s_id INT)
RETURNS VOID AS
    $$
    BEGIN
        INSERT INTO likes (user_id, song_id)  VALUES (u_id, s_id);
    end;
    $$
LANGUAGE plpgsql;

SELECT add_like(2,9);
SELECT * FROM likes;


CREATE OR REPLACE FUNCTION get_likes(u_id INT)
RETURNS TABLE (
    user_id int,
    song_id int
)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY EXECUTE 'SELECT * FROM likes WHERE user_id = CAST($1 AS int)' USING u_id;
END;
$$;

SELECT * FROM get_likes('1');