package edu.montana.csci.csci440.homework;

import edu.montana.csci.csci440.DBTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Homework3 extends DBTest {

    @Test
    /*
     * Create a view tracksPlus to display the artist, song title, album, and genre for all tracks.
     */
    public void createTracksPlusView(){
        //TODO fill this in
        executeDDL("CREATE VIEW tracksPlus AS\n" +
                "SELECT tracks.TrackId as TrackID, artists.Name as ArtistName, tracks.Name as Title, albums.Title as AlbumTitle, genres.Name as GenreName\n" +
                "FROM playlist_track\n" +
                "JOIN tracks on playlist_track.TrackId = tracks.TrackId\n" +
                "Join albums on tracks.AlbumId = albums.AlbumId\n" +
                "JOIN artists on albums.ArtistId = artists.ArtistId\n" +
                "JOIN genres on tracks.GenreId = genres.GenreId\n" +
                "GROUP BY tracks.TrackId;");


        List<Map<String, Object>> results = executeSQL("SELECT * FROM tracksPlus ORDER BY TrackId");
        assertEquals(3503, results.size());
        assertEquals("Rock", results.get(0).get("GenreName"));
        assertEquals("AC/DC", results.get(0).get("ArtistName"));
        assertEquals("For Those About To Rock We Salute You", results.get(0).get("AlbumTitle"));
    }

    @Test
    /*
     * Create a table grammy_infos to track grammy information for an artist.  The table should include
     * a reference to the artist, the album (if the grammy was for an album) and the song (if the grammy was
     * for a song).  There should be a string column indicating if the artist was nominated or won.  Finally,
     * there should be a reference to the grammy_category table
     *
     * Create a table grammy_category
     */
    public void createGrammyInfoTable(){
        //TODO fill these in

        //executeDDL("create table grammy_categories");
        executeDDL("CREATE TABLE grammy_categories (\n" +
                "    GrammyCategoryId  INTEGER  NOT NULL PRIMARY KEY,\n" +
                "    Name  NVARCHAR(220)\n" +
                ");");


        //executeDDL("create table grammy_infos");
        executeDDL("CREATE TABLE grammy_infos (\n" +
                "    grammyId  INTEGER  not null PRIMARY KEY,\n" +
                "    Status  NVARCHAR(220),\n" +
                "    ArtistId  INTEGER,\n" +
                "    AlbumId  INTEGER,\n" +
                "    TrackId  INTEGER,\n" +
                "    GrammyCategoryId INTEGER,\n" +
                "    FOREIGN KEY (ArtistId) references artists(ArtistId)\n" +
                "        ON DELETE CASCADE\n" +
                "        ON UPDATE NO ACTION,\n" +
                "    \n" +
                "    FOREIGN KEY (AlbumId) references  albums(AlbumId)\n" +
                "        ON DELETE CASCADE\n" +
                "        ON UPDATE NO ACTION,\n" +
                "    \n" +
                "    FOREIGN KEY (TrackId) references tracks(TrackId)\n" +
                "        ON DELETE CASCADE\n" +
                "        ON UPDATE NO ACTION,\n" +
                "    \n" +
                "    FOREIGN KEY (GrammyCategoryId) references grammy_categories(GrammyCategoryId)\n" +
                "        ON DELETE CASCADE\n" +
                "        ON UPDATE NO ACTION\n" +
                ");");


        // TEST CODE
        executeUpdate("INSERT INTO grammy_categories(Name) VALUES ('Greatest Ever');");
        Object categoryId = executeSQL("SELECT GrammyCategoryId FROM grammy_categories").get(0).get("GrammyCategoryId");

        executeUpdate("INSERT INTO grammy_infos(ArtistId, AlbumId, TrackId, GrammyCategoryId, Status) VALUES (1, 1, 1, " + categoryId + ",'Won');");

        List<Map<String, Object>> results = executeSQL("SELECT * FROM grammy_infos");
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).get("ArtistId"));
        assertEquals(1, results.get(0).get("AlbumId"));
        assertEquals(1, results.get(0).get("TrackId"));
        assertEquals(1, results.get(0).get("GrammyCategoryId"));
    }

    @Test
    /*
     * Bulk insert five categories of your choosing in the genres table
     */
    public void bulkInsertGenres(){
        Integer before = (Integer) executeSQL("SELECT COUNT(*) as COUNT FROM genres").get(0).get("COUNT");

        //TODO fill this in
        executeUpdate("INSERT INTO genres (Name)\n" +
                "VALUES\n" +
                "(\"YEETERZ\"),\n" +
                "(\"BALLERZ\"),\n" +
                "(\"YOPPAZ\"),\n" +
                "(\"GEMREZZ\"),\n" +
                "(\"DUBEDUFOO\");");

        Integer after = (Integer) executeSQL("SELECT COUNT(*) as COUNT FROM genres").get(0).get("COUNT");
        assertEquals(before + 5, after);
    }

}
