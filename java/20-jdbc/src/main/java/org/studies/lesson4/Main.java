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
import java.util.List;
import java.util.Objects;
import javax.xml.transform.Result;

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
            String sql = "SELECT * FROM music.albumview WHERE artist_name = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "ELF");
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

    private static int addSong(PreparedStatement ps, Connection connection, int albumId,
        int trackNo, String songTitle) throws SQLException {

        int songId = -1;
        ps.setInt(1, albumId);
        ps.setInt(2, trackNo);
        ps.setString(3, songTitle);
        int insertedCount = ps.executeUpdate();
        if (insertedCount > 0) {
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                songId = generatedKeys.getInt(1);
                System.out.println("Auto-increment ID: " + songId);
            }
        }
        return songId;
    }

    private static void addDataFromFile(Connection connection) throws SQLException {
        List<String> records = null;

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
    }

}
