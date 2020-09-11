SELECT name FROM artists
WHERE name LIKE '%A%';

SELECT name, COUNT(artists.ArtistId) AS idCount
from artists
JOIN albums ON albums.ArtistId = artists.ArtistId
GROUP BY albums.ArtistId
HAVING COUNT(albums.ArtistId) > 1;

Select tracks.name as TrackName, albums.title as AlbumTitle, artists.name as ArtistName
from tracks
JOIN albums on tracks.AlbumId = albums.AlbumId
Left OUTER JOIN artists on albums.AlbumId = artists.artistID
where tracks.Milliseconds > 600000;

SELECT tracks.name from tracks where tracks.milliseconds > 600000;
