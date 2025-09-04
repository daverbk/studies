package org.studies.lesson2;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.postgresql.ds.PGSimpleDataSource;

public class Main {

    public static void main(String[] args) {

        Properties props = new Properties();
        try {
            props.load(Main.class.getClassLoader().getResourceAsStream("music.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        var dataSource = new PGSimpleDataSource();
        dataSource.setServerName(props.getProperty("server"));
        dataSource.setPortNumber(Integer.parseInt(props.getProperty("port")));
        dataSource.setDatabaseName(props.getProperty("database"));

        String query = "SELECT * FROM music.artists";

        try (
            var connection = dataSource.getConnection(props.getProperty("user"),
                System.getenv("PG_PASSWORD"));
            Statement statement = connection.createStatement();
        ) {
            statement.setMaxRows(10);
            ResultSet resultSet = statement.executeQuery(query);

            var meta = resultSet.getMetaData();
            System.out.println("----------------");

            for (int i = 1; i <= meta.getColumnCount(); i++) {
                System.out.printf("%-15s", meta.getColumnName(i).toUpperCase());
            }
            System.out.println();

            while (resultSet.next()) {
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    System.out.printf("%-15s", resultSet.getString(i));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
