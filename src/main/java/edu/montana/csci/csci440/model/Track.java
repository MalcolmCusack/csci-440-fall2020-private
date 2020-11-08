package edu.montana.csci.csci440.model;

import edu.montana.csci.csci440.util.DB;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Track extends Model {

    private Long trackId;
    private Long albumId;
    private Long mediaTypeId;
    private Long genreId;
    private String name;
    private Long milliseconds;
    private Long bytes;
    private BigDecimal unitPrice;
    private String artistName;
    private String albumTitle;

    public static final String REDIS_CACHE_KEY = "cs440-tracks-count-cache";

    public Track() {
        // new track for insert
        mediaTypeId = 1l;
        genreId = 1l;
        milliseconds = 0l;
        bytes = 0l;
        unitPrice = new BigDecimal(0);
    }

    private Track(ResultSet results) throws SQLException {
        name = results.getString("Name");
        milliseconds = results.getLong("Milliseconds");
        bytes = results.getLong("Bytes");
        unitPrice = results.getBigDecimal("UnitPrice");
        trackId = results.getLong("TrackId");
        albumId = results.getLong("AlbumId");
        mediaTypeId = results.getLong("MediaTypeId");
        genreId = results.getLong("GenreId");
        artistName = results.getString("artistName");
        albumTitle = results.getString("albumTitle");
    }

    public static Track find(int i) {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT *, artists.Name as artistName, albums.Title as albumTitle FROM tracks " +
                     "JOIN albums on tracks.AlbumId = albums.AlbumId\n" +
                     "JOIN artists on albums.ArtistId = artists.ArtistId\n" +
                     "WHERE trackId = ?;")) {
            stmt.setLong(1, i);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                return new Track(results);
            } else {
                return null;
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static Long count() {
        Jedis redisClient = new Jedis(); // use this class to access redis and create a cache
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) as Count FROM tracks\n")) {
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                return results.getLong("Count");
            } else {
                throw new IllegalStateException("Should find a count!");
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public Album getAlbum() {
        return Album.find(albumId);
    }

    public MediaType getMediaType() {
        return null;
    }

    public Genre getGenre() {
        return null;
    }

    public List<Playlist> getPlaylists(){
        return Collections.emptyList();
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    public Long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(Long milliseconds) {

        this.milliseconds = milliseconds;
    }

    public Long getBytes() {
        return bytes;
    }

    public void setBytes(Long bytes) {
        this.bytes = bytes;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public void setAlbum(Album album) {
        albumId = album.getAlbumId();
    }

    public Long getMediaTypeId() {
        return mediaTypeId;
    }

    public void setMediaTypeId(Long mediaTypeId) {
        this.mediaTypeId = mediaTypeId;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }


    @Override
    public boolean verify() {
        _errors.clear();
        if (name == null || "".equals(name)) {
            addError("name can't be null or blank!");
        }
        if (albumId == null ) {
            addError("Need to select an Album");
        }
        return !hasErrors();
    }

    @Override
    public boolean create() {
        if (verify()) {


            try (Connection conn = DB.connect();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO tracks ( Name, AlbumId, Milliseconds, Bytes, UnitPrice, MediaTypeId, GenreId) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setString(1, this.getName());
                stmt.setLong(2, this.getAlbumId() );
                stmt.setLong(3, this.getMilliseconds());
                stmt.setLong(4, this.getBytes());
                stmt.setBigDecimal(5, this.getUnitPrice());
                stmt.setLong(6, this.getMediaTypeId());
                stmt.setLong(7, this.getGenreId());
                stmt.executeUpdate();
                trackId = DB.getLastID(conn);
                return true;
            } catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        } else {
            return false;
        }
    }

    // WTF??
    @Override
    public boolean update() {
        if (verify()) {

            try (Connection conn = DB.connect();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE tracks SET Name=?, AlbumId=?, Milliseconds=?, Bytes=?, UnitPrice=?, MediaTypeId=?, GenreId=?  WHERE TrackId=?")) {
                stmt.setString(1, this.getName());
                stmt.setLong(2, this.getAlbumId());
                stmt.setLong(3, this.getMilliseconds());
                stmt.setLong(4, this.getBytes());
                stmt.setBigDecimal(5, this.getUnitPrice());
                stmt.setLong(6, this.getMediaTypeId());
                stmt.setLong(7, this.getGenreId());
                stmt.setLong(8, this.getTrackId());
                stmt.executeUpdate();
                return true;
            } catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        } else {
            return false;
        }
    }

    @Override
    public void delete() {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM tracks WHERE TrackId=?")) {
            stmt.setLong(1, this.getTrackId());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    // build a query
    public static List<Track> advancedSearch(int page, int count,
                                             String search, Integer artistId, Integer albumId,
                                             Integer maxRuntime, Integer minRuntime, Integer mediaTypeId,
                                              Integer genreId) {
        LinkedList<Object> args = new LinkedList<>();

        String query = "SELECT *, artists.Name as artistName, albums.Title as albumTitle FROM tracks " +
                "JOIN albums ON tracks.AlbumId = albums.AlbumId " +
                "JOIN artists ON albums.ArtistId = artists.ArtistId " +
                "WHERE tracks.Name LIKE ?";

        args.add("%" + search + "%");

        // Conditionally include the query and argument
        if (artistId != null) {
            query += " AND albums.ArtistId=? ";
            args.add(artistId);
        }

        if (albumId != null) {
            query += " AND albums.AlbumId=?";
            args.add(albumId);
        }

        if (mediaTypeId != null) {
            query += " AND tracks.MediaTypeId=?";
            args.add(mediaTypeId);
        }

        if (genreId != null) {
            query += " AND tracks.GenreId=?";
            args.add(genreId);
        }

        // TODO add math for seconds to milliseconds
        if (minRuntime != null) {
            query += " AND tracks.Milliseconds > ?";
            args.add(minRuntime);
        }

        if (maxRuntime != null) {
            query += " AND tracks.Milliseconds < ?";
            args.add(maxRuntime);
        }

        query += " LIMIT ? OFFSET ?";
        args.add(count);
        args.add((page - 1) * count);

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < args.size(); i++) {
                Object arg = args.get(i);
                stmt.setObject(i + 1, arg);
            }
            ResultSet results = stmt.executeQuery();
            List<Track> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new Track(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static List<Track> search(int page, int count, String orderBy, String search) {
        String query = "SELECT *, artists.Name as artistName, albums.Title as albumTitle FROM tracks " +
                "JOIN albums ON tracks.AlbumId = albums.AlbumId " +
                "JOIN artists ON albums.ArtistId = artists.ArtistId " +
                " WHERE tracks.Name LIKE ? ";

        search = ("%" + search + "%");

        if (orderBy == null) {
            query += "";
        }
        if (orderBy == "Milliseconds") {
            query += " ORDER BY Milliseconds ";
        }

        if (orderBy == "Bytes") {
            query += " ORDER BY Bytes ";
        }

        query += " LIMIT ? OFFSET ? ";

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, search);
            stmt.setInt(2, count);
            stmt.setInt(3, (page - 1) * count);
            ResultSet results = stmt.executeQuery();
            List<Track> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new Track(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static List<Track> forAlbum(Long albumId) {
        String query = "SELECT *, artists.Name as artistName, albums.Title as albumTitle FROM tracks\n" +
                "JOIN albums on tracks.AlbumId = albums.AlbumId " +
                "JOIN artists on albums.ArtistId = artists.ArtistId " +
                "WHERE tracks.AlbumId=?";
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, albumId);
            ResultSet results = stmt.executeQuery();
            List<Track> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new Track(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    // Sure would be nice if java supported default parameter values
    public static List<Track> all() {
        return all(0, Integer.MAX_VALUE);
    }

    public static List<Track> all(int page, int count) {
        return all(page, count, "TrackId");
    }

    public static List<Track> all(int page, int count, String orderBy) {

        String query = "SELECT *, artists.Name as artistName, albums.Title as albumTitle FROM tracks " +
                "JOIN albums on tracks.AlbumId = albums.AlbumId " +
                "JOIN artists on albums.ArtistId = artists.ArtistId ";

        if (orderBy == null) {
            query += "";
        }

        if (orderBy == "Milliseconds") {
            query += " ORDER BY Milliseconds ";
        }

        if (orderBy == "Bytes") {
            query += " ORDER BY Bytes ";
        }

        query += " LIMIT ? OFFSET ? ";

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, count);
            stmt.setInt(2, (page - 1) * count);
            ResultSet results = stmt.executeQuery();
            List<Track> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new Track(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

}
