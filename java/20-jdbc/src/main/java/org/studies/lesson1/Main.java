package org.studies.lesson1;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import org.postgresql.ds.PGSimpleDataSource;

public class Main {

    private final static String CONN_STRING = "jdbc:postgresql://localhost:5432/music";

    public static void main(String[] args) {
        String username = JOptionPane.showInputDialog(null, "Enter db username");
        JPasswordField pf = new JPasswordField();
        int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter db password",
            JOptionPane.OK_CANCEL_OPTION);
        final char[] password = (okCxl == JOptionPane.OK_OPTION) ? pf.getPassword() : null;

        var dataSource = new PGSimpleDataSource();
//        dataSource.setURL(CONN_STRING);
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("music");

//        try (Connection connection = DriverManager.getConnection(CONN_STRING, username,
//            String.valueOf(password)
//        )) {

        try (Connection connection = dataSource.getConnection(username, String.valueOf(password))) {
            System.out.println("Successfully connected to the db.");
            Arrays.fill(password, ' ');
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
