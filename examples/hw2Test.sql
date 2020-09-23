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


/*Order By AlbumTitle; */
