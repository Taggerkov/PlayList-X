INSERT INTO Song (id, title, artist, year, albumName, featuredArtist, duration, genre, link) VALUES

(1, 'Stairway to heaven', 'Led Zeppelin', 2005, 'Led Zeppelin IV', null,  '482', 'Rock', 'https://www.youtube.com/watch?v=QkF3oxziUI4'),
(2, 'Supermassive Black Hole', 'Muse', 2006, 'Black Holes and Revelations', null,  '210', 'Rock', 'https://www.youtube.com/watch?v=Xsp3_a-PMTw'),
(3, 'La vereda de la puerta de atrás', 'Extremoduro', 2002, 'Yo, Minoría Absoluta', null, '243', 'Rock', 'https://www.youtube.com/watch?v=43S_qfT6vpo'),
(4, 'Zu Atrapatu Arte', 'Kortatu', 1983, 'A Frontline Compilation', null, '109', 'Punk', 'https://www.youtube.com/watch?v=3gRCUbiM-QI'),
(5, 'Disorder', 'Joy Division', 1979, 'Unknown Pleasures', null, '212', 'Post-Punk', 'https://www.youtube.com/watch?v=9ryJB-FF_Jg'),
(6, 'Судно', 'Молчат Дома', 2018, 'Этажи', null, '141', 'Post-Punk', 'https://www.youtube.com/watch?v=s1ATTIQrmIQ'),
(7, 'Dos Adolescentes y su Primer Amor', 'Depresión Sonora', 2022, 'El Arte de Morir muy Despacio', null, '218', 'Post-Punk', 'https://www.youtube.com/watch?v=Kcoy8dVStDk'),
(8, 'It Was A Good Day', 'Ice Cube', 1992, 'The Predator', null, '307', 'Rap', 'https://www.youtube.com/watch?v=h4UqMyldS7Q'),
(9, 'Bohemian Rhapsody', 'Queen', 1975, 'A Night At The Opera', null, '360', 'Rock', 'https://www.youtube.com/watch?v=fJ9rUzIMcZQ'),
(10, 'Vivir para Contarlo', 'Violadores Del Verso', 2006, 'Vivir para Contarlo', null, '346', 'Rap', 'https://www.youtube.com/watch?v=brwIP1wI-FA'),
(11,'C.R.E.A.M', 'Wu-Tang Clan', 1993, 'Enter The Wu-Tang', 'Method Man', '252', 'Rap', 'https://youtu.be/PBwAxmrE194?si=-il7rQ7SazE6dFTc'),
(12, 'Ms. Jackson', 'Outkast', 2000, 'Stankonia', null,  '270', 'Rap', 'https://youtu.be/MYxAiK6VnXw?si=TTUxnJRfEYjW0lOu'),
(13, 'Himno de Ohio', 'Don Pollo', 2024, 'El que Quiera', 'DJ Goofy', '60', 'Romantic Gospel', 'https://youtu.be/I7jx59ebS60?si=cFEDqg6MJbNFFYT7'),
(14, 'Master of Puppets', 'Metallica', 1986, 'Master of Puppets', null, '517', 'Metal', 'https://youtu.be/E0ozmU9cJDg?si=ik58RoVbfbOHyRwP'),
(15, 'The Trooper', 'Iron Maiden', 1983, 'Piece of Mind', null, '264', 'Metal', 'https://youtu.be/X4bgXH3sJ2Q?si=GLsJ2VZzJYTKUo62'),
(16, 'Toxicity', 'System Of A Down', 2001, 'Toxicity', null, '225', 'Metal', 'https://youtu.be/iywaBOMvYLI?si=JTVFTOKDRvXAwkA-');


INSERT INTO Listener(id, username, isAdmin, hash, salt) VALUES

(0, 'PlayListX', true, 'hsdauih', 'snduaie'),
(1, 'ElMiuler', true, 'asjde','ksakowme'),
(2, 'MarieteColoquete', true, 'alsjda','nkfonejn'),
(3, 'RazzBan', true, 'aksdnke', 'meirnkqw'),
(4, 'Taggerkov', true, 'dekndke', 'mfkelkwne'),
(5, 'PepeViyuela', false, 'asponkq', 'qpwkenr');

INSERT INTO Playlist(id, title, ownerId, creationDate, songsCount, isPublic, totalDuration) VALUES
(1, 'Rock', 0, '2024-04-25', default, true, default ),
(2, 'Post-Punk', 0, '2024-04-25', default, true, default ),
(3, 'Rap', 0, '2024-04-25', default, true, default);


INSERT INTO songList(playlist_id, song_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 9), (2, 5), (2, 6), (2, 7);

INSERT INTO likes(user_id, song_id) VALUES
(1,2), (1,7);