package org.studies.lesson4;

import static org.studies.lesson2.MusicDML.printRecords;
import static org.studies.lesson3.Main.setUpDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {

    private static String ARTIST_INSERT = "INSERT INTO music.artists (artist_name) VALUES (?)";
    private static String ALBUM_INSERT = "INSERT INTO music.albums (artist_id, album_name) values (?, ?)";
    private static String SONG_INSERT = "INSERT INTO music.songs (album_id, track_number, song_title) VALUES (?, ?, ?)";

    public static void main(String[] args) {
        var dataSource = setUpDataSource("music");
        try (Connection connection = dataSource.getConnection(
            System.getenv("PG_USERNAME"),
            System.getenv("PG_PASSWORD")
        )) {
            addDataFromFile(connection);

            String sql = "SELECT * FROM music.albumview WHERE artist_name = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "Bob Dylan");
            ResultSet resultSet = ps.executeQuery();
            printRecords(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int addArtist(PreparedStatement ps, String artistName) throws SQLException {
        int artistId = -1;
        ps.setString(1, artistName);
        int insertedCount = ps.executeUpdate();
        if (insertedCount > 0) {
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                artistId = generatedKeys.getInt(1);
                System.out.println("Auto-increment ID: " + artistId);
            }
        }
        return artistId;
    }

    private static int addAlbum(PreparedStatement ps, int artistId,
        String albumName) throws SQLException {

        int albumId = -1;
        ps.setInt(1, artistId);
        ps.setString(2, albumName);
        int insertedCount = ps.executeUpdate();
        if (insertedCount > 0) {
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                albumId = generatedKeys.getInt(1);
                System.out.println("Auto-increment ID: " + albumId);
            }
        }
        return albumId;
    }

    private static void addSong(PreparedStatement ps, Connection connection, int albumId,
        int trackNo, String songTitle) throws SQLException {

        ps.setInt(1, albumId);
        ps.setInt(2, trackNo);
        ps.setString(3, songTitle);
        ps.addBatch();
    }

    private static void addDataFromFile(Connection connection) throws SQLException {
        List<String> records;
        try {
            records = Files.readAllLines(
                Path.of(
                    Objects.requireNonNull(
                        org.studies.lesson2.Main.class.getClassLoader().getResource("NewAlbums.csv")
                    ).getPath()
                )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String lastAlbum = null;
        String lastArtist = null;
        int artistId = -1;
        int albumId = -1;
        try (
            PreparedStatement psArtist = connection.prepareStatement(ARTIST_INSERT,
                Statement.RETURN_GENERATED_KEYS);
            PreparedStatement psAlbum = connection.prepareStatement(ALBUM_INSERT,
                Statement.RETURN_GENERATED_KEYS);
            PreparedStatement psSong = connection.prepareStatement(SONG_INSERT,
                Statement.RETURN_GENERATED_KEYS)

        ) {
            connection.setAutoCommit(false);
            for (String record : records) {
                String[] columns = record.split(",");
                if (lastArtist == null || !lastArtist.equals(columns[0])) {
                    lastArtist = columns[0];
                    artistId = addArtist(psArtist, lastArtist);
                }
                if (lastAlbum == null || !lastAlbum.equals(columns[1])) {
                    lastAlbum = columns[1];
                    albumId = addAlbum(psAlbum, artistId, lastAlbum);
                }
                addSong(psSong, connection, albumId, Integer.parseInt(columns[2]), columns[3]);
            }
            int[] inserts = psSong.executeBatch();
            System.out.printf("%d song records added %n", inserts.length);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }
}
