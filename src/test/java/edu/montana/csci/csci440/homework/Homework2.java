package edu.montana.csci.csci440.homework;

import edu.montana.csci.csci440.DBTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class Homework2 extends DBTest {

    @Test
    /*
     * Write a query in the string below that returns all artists that have an 'A' in their name
     */
    void selectArtistsWhoseNameHasAnAInIt(){
        List<Map<String, Object>> results = executeSQL("SELECT name FROM artists\n" +
                "WHERE name LIKE '%A%';");
        assertEquals(211, results.size());
    }

    @Test
    /*
     * Write a query in the string below that returns all artists that have more than one album
     */
    void selectAllArtistsWithMoreThanOneAlbum(){
        List<Map<String, Object>> results = executeSQL(
                "SELECT name, COUNT(artists.ArtistId) AS idCount\n" +
                        "from artists\n" +
                        "JOIN albums ON albums.ArtistId = artists.ArtistId\n" +
                        "GROUP BY albums.ArtistId\n" +
                        "HAVING COUNT(albums.ArtistId) > 1;");

        assertEquals(56, results.size());
        assertEquals("AC/DC", results.get(0).get("Name"));
    }

    @Test
        /*
         * Write a query in the string below that returns all tracks longer than six minutes along with the
         * album and artist name
         */


    void selectTheTrackAndAlbumAndArtistForAllTracksLongerThanSixMinutes() {
        List<Map<String, Object>> results = executeSQL(
                "Select tracks.name as TrackName, albums.title as AlbumTitle, artists.name as ArtistName\n" +
                        "FROM tracks\n" +
                        "JOIN albums on tracks.AlbumId = albums.AlbumId\n" +
                        "Join artists on albums.ArtistId = artists.artistID\n" +
                        "where tracks.Milliseconds > 360000;");

        assertEquals(623, results.size());

        // For now just get the count right, we'll do more elaborate stuff when we get
        // to ORDER BY
/*
        assertEquals("Princess of the Dawn", results.get(0).get("TrackName"));
        assertEquals("Restless and Wild", results.get(0).get("AlbumTitle"));
        assertEquals("Accept", results.get(0).get("ArtistsName"));

        assertEquals("Snoopy's search-Red baron", results.get(11).get("TrackName"));
        assertEquals("The Best Of Billy Cobham", results.get(11).get("AlbumTitle"));
        assertEquals("Billy Cobham", results.get(11).get("ArtistsName"));
*/
    }


}
