SELECT name FROM artists
WHERE name LIKE '%A%';

SELECT name, COUNT(artists.ArtistId) AS idCount
from artists
JOIN albums ON albums.ArtistId = artists.ArtistId
GROUP BY albums.ArtistId
HAVING COUNT(albums.ArtistId) > 1;

Select tracks.name as TrackName, albums.title as AlbumTitle, artists.name as ArtistName
FROM tracks
JOIN albums on tracks.AlbumId = albums.AlbumId
Join artists on albums.ArtistId = artists.artistID
where tracks.Milliseconds > 360000;

SELECT albums.AlbumId, albums.Title from albums
JOIN artists on albums.ArtistId = artists.ArtistId
where artists.ArtistId = 1;

SELECT artists.Name as Artist, tracks.Name as Title, albums.Title as Album, genres.Name as Genre  from tracks
JOIN artists on tracks.Composer = artists.Name
Join albums on tracks.AlbumId = albums.AlbumId
JOIN genres on tracks.GenreId = genres.GenreId;

SELECT tracks.Name from playlist_track
Join tracks on playlist_track.TrackId = tracks.TrackId;



SELECT tracks.TrackId as TrackID, artists.Name as Artist, tracks.Name as Title, albums.Title as Album, genres.Name as Genre, playlists.Name
FROM playlists
JOIN playlist_track on playlists.PlaylistId = playlist_track.PlaylistId
JOIN tracks on playlist_track.TrackId = tracks.TrackId
Join albums on tracks.AlbumId = albums.AlbumId
JOIN artists on albums.ArtistId = artists.ArtistId
JOIN genres on tracks.GenreId = genres.GenreId
GROUP BY tracks.TrackId;

CREATE TABLE grammy_info (
    grammyId  INTEGER  not null PRIMARY KEY,
    Status  NVARCHAR(220),
    ArtistId  INTEGER,
    AlbumId  INTEGER,
    TrackId  INTEGER,
    GrammyCategoryId INTEGER,
    FOREIGN KEY (ArtistId) references artists(ArtistId)
        ON DELETE CASCADE
        ON UPDATE NO ACTION,

    FOREIGN KEY (AlbumId) references  albums(AlbumId)
        ON DELETE CASCADE
        ON UPDATE NO ACTION,

    FOREIGN KEY (TrackId) references tracks(TrackId)
        ON DELETE CASCADE
        ON UPDATE NO ACTION,

    FOREIGN KEY (GrammyCategoryId) references grammy_categories(GrammyCategoryId)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
);

CREATE TABLE grammy_categories (
    GrammyCategoryId  INTEGER  NOT NULL PRIMARY KEY,
    Name  NVARCHAR(220)
);

SELECT last_insert_rowid();

INSERT INTO genres (Name)
VALUES
("YEETERZ"),
("BALLERZ"),
("YOPPAZ"),
("GEMREZZ"),
("DUBEDUFOO");


SELECT c.FirstName as CustomerFirstName, c.LastName as CustomerLastName, e.FirstName as EmployeeName, e.LastName as EmployeeLastName, b.FirstName as bossFirstName, b.LastName as bossLastName
FROM invoices
JOIN customers c on invoices.CustomerId = c.CustomerId
JOIN employees e on c.SupportRepId = e.EmployeeId
JOIN employees b on e.ReportsTo = b.EmployeeId
GROUP BY invoices.CustomerId
HAVING COUNT(invoices.CustomerId) > 1;

CREATE TABLE student
(
    StudentId INTEGER not null PRIMARY KEY,
    Email     NVARCHAR(100),
    NetId     NVARCHAR(50)
);

CREATE TABLE class
(
    ClassId INTEGER NOT NULL  PRIMARY KEY ,
    Name NVARCHAR(100),
    StudentEmails NVARCHAR(100),
    StudentId INTEGER,
    FOREIGN KEY (StudentId) REFERENCES  student (StudentId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

SELECT * FROM tracks WHERE name LIKE "Let's Get It Up" LIMIT 1

SELECT * FROM tracks
JOIN albums ON tracks.AlbumId = albums.AlbumId
WHERE name Like "For Those About To Rock (We Salute You)"
AND albums.ArtistId=1
AND albums.AlbumId=1
AND tracks.MediaTypeId = 1
AND tracks.GenreId = 1
AND tracks.Milliseconds > 0
AND tracks.Milliseconds < 1000000000
LIMIT 5 OFFSET 10;
