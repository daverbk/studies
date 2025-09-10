package org.studies.task2;

import static org.studies.lesson3.Main.setUpDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Main {

    private static final String INSERT_ORDER = "INSERT INTO storefront.order (order_date) VALUES (?)";
    private static final String INSERT_ORDER_DETAILS = "INSERT INTO storefront.order_details (order_id, item_description, quantity) VALUES (?, ?, ?)";

    public static void main(String[] args) {

        var ds = setUpDataSource("storefront");

        try (Connection connection = ds.getConnection()) {
//            addQuantityTable(connection);
            addDataFromFile(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addQuantityTable(Connection connection) {
        String sql = "ALTER TABLE storefront.order_details ADD COLUMN quantity INT";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("order_details has been successfully altered%n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int insertOder(
        Connection connection,
        PreparedStatement preparedStatement,
        Date orderDate
    ) throws SQLException {

        var orderId = -1;

        try {
            connection.setAutoCommit(false);
            preparedStatement.setDate(1, orderDate);
            var inserted = preparedStatement.executeUpdate();
            if (inserted > 0) {
                var resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    orderId = resultSet.getInt(1);
                    System.out.printf("Order %d has been successfully inserted%n", orderId);
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }

        return orderId;
    }

    private static void insertOrderDetails(
        PreparedStatement preparedStatement,
        String description,
        int orderId,
        int quantity
    ) throws SQLException {

        preparedStatement.setInt(1, orderId);
        preparedStatement.setString(2, description);
        preparedStatement.setInt(3, quantity);
        preparedStatement.addBatch();
    }

    private static void addDataFromFile(Connection connection) throws SQLException {
        List<String> records;
        try {
            records = Files.readAllLines(
                Path.of(
                    Objects.requireNonNull(
                        org.studies.lesson2.Main.class.getClassLoader().getResource("Orders.csv")
                    ).getPath()
                )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String lastOrder = null;
        int orderId = -1;

        try (
            PreparedStatement psOrder = connection.prepareStatement(
                INSERT_ORDER,
                Statement.RETURN_GENERATED_KEYS
            );
            PreparedStatement psOrderDetails = connection.prepareStatement(
                INSERT_ORDER_DETAILS,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {
            for (String record : records) {
                String[] columns = record.split(",");

                if (columns[0].equals("order") && (lastOrder == null || !lastOrder.equals(
                    columns[1]))) {
                    lastOrder = columns[1];
                    orderId = insertOder(connection, psOrder, Date.valueOf(
                        LocalDate.parse(columns[1],
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
                }

                if (columns[0].equals("item")) {
                    insertOrderDetails(
                        psOrderDetails,
                        columns[2],
                        orderId,
                        Integer.parseInt(columns[1])
                    );
                }

            }

            int[] inserts = psOrderDetails.executeBatch();
            System.out.printf("%d order details added%n", inserts.length);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
