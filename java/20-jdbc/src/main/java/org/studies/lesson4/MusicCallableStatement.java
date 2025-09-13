package org.studies.lesson4;

import static org.studies.lesson2.MusicDML.printRecords;
import static org.studies.lesson3.Main.setUpDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MusicCallableStatement {

    private static final int ARTIST_COLUMN = 0;
    private static final int ALBUM_COLUMN = 1;
    private static final int SONG_COLUMN = 3;

    public static void main(String[] args) {
        Map<String, Map<String, String>> albums = null;
        try (var lines = Files.lines(
            Path.of(
                Objects.requireNonNull(
                    org.studies.lesson2.Main.class.getClassLoader().getResource("NewAlbums.csv")
                ).getPath()
            )
        )) {
            albums = lines.map(s -> s.split(",")).collect(
                Collectors.groupingBy(s -> s[ARTIST_COLUMN],
                    Collectors.groupingBy(s -> s[ALBUM_COLUMN],
                        Collectors.mapping(s -> s[SONG_COLUMN], Collectors.joining(
                            "\",\"", "[\"", "\"]"
                        )))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        albums.forEach((artist, artistsAlbums) -> artistsAlbums.forEach(
            (key, value) -> System.out.println(key + " : " + value)));

        var dataSource = setUpDataSource("music");
        try (Connection connection = dataSource.getConnection(
            System.getenv("PG_USERNAME"),
            System.getenv("PG_PASSWORD")
        )) {
            CallableStatement cs = connection.prepareCall(
                "CALL music.add_album_return_counts(?::text,?::text,?::jsonb,?)");
            albums.forEach((artist, albumMap) -> albumMap.forEach((album, songs) -> {
                try {
                    cs.setString(1, artist);
                    cs.setString(2, album);
                    cs.setString(3, songs);
                    cs.registerOutParameter(4, Types.INTEGER);
                    cs.execute();
                    System.out.printf("%d songs were added for %s%n", cs.getInt(4), album);

                } catch (SQLException e) {
                    System.err.println(e.getErrorCode() + " " + e.getMessage());
                }
            }));

            String sql = "SELECT * FROM music.albumview WHERE artist_name = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "Bob Dylan");
            ResultSet resultSet = ps.executeQuery();
            printRecords(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
