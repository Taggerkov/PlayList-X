/*-----------------------------------------------THE LAB---------------------------------------------------------------------*/

/*
SELECT *
from Playlist
WHERE Playlist.id=1;

INSERT INTO songList(playlist_id, song_id) VALUES
(1, 7);


SELECT *
FROM songList;


SELECT count(song_id) FROM song, songList, Playlist WHERE Song.id=song_id and playlist_id=Playlist.id;

SELECT sum(duration) FROM song, songList WHERE Song.id=song_id;


SELECT song_id, title
FROM songList, song
WHERE playlist_id=1 and id = songList.song_id;
DROP SCHEMA PlayListX CASCADE ;*/

SELECT *
FROM playlist;