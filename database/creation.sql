CREATE SCHEMA PlayListX;
SET SCHEMA 'playlistx';
CREATE TABLE Song(
    id smallint primary key,
    title varchar,
    artist varchar,
    year int,
    albumName varchar(50),
    featuredArtist varchar,
    duration int,

    genre varchar,
    link varchar,
    CONSTRAINT year_check check (year > 0 AND year <= 2024)
);

CREATE TABLE Listener
(
    id smallint primary key,
    username varchar(20),
    isAdmin boolean,
    hash bytea,
    salt bytea
);


CREATE TABLE Playlist(
    id smallint primary key,
    title varchar,
    ownerId smallint,
    creationDate date default current_date,
    songsCount int default 0,
    isPublic boolean,
    totalDuration int default 0,
    FOREIGN KEY (ownerId) REFERENCES Listener (id)
);

CREATE TABLE collaborator(
    listener_id int,
    playlist_id int,
    FOREIGN KEY (playlist_id) REFERENCES Playlist (id),
    FOREIGN KEY (listener_id) REFERENCES Listener (id)
);

CREATE TABLE songList(
    playlist_id int,
    song_id int,
    FOREIGN KEY (playlist_id) REFERENCES Playlist (id),
    FOREIGN KEY (song_id) REFERENCES Song (id)
);

CREATE TABLE likes
(
    user_id int,
    song_id int,
    FOREIGN KEY (song_id) REFERENCES Song (id),
    FOREIGN KEY (user_id) REFERENCES Listener (id)
);