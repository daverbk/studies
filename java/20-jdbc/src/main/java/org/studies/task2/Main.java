package org.studies.task2;

import static org.studies.lesson3.Main.setUpDataSource;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static final String INSERT_ORDER = "INSERT INTO storefront.order (order_date) VALUES (?::timestamp)";
    private static final String INSERT_ORDER_DETAILS = "INSERT INTO storefront.order_details (order_id, item_description, quantity) VALUES (?, ?, ?)";

    public static void main(String[] args) {

        var ds = setUpDataSource("storefront");
        List<Order> orders = readData();
        try (Connection connection = ds.getConnection()) {
//            addQuantityTable(connection);
            addOrders(connection, orders);
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

    public static List<Order> readData() {
        List<Order> vals = new ArrayList<>();
        try (Scanner scanner = new Scanner(Path.of(
            Objects.requireNonNull(
                org.studies.lesson2.Main.class.getClassLoader().getResource("Orders.csv")
            ).getPath()
        ))) {
            scanner.useDelimiter("[,\\n]");
            var list = scanner.tokens().map(String::trim).toList();
            for (int i = 0; i < list.size(); i++) {
                String value = list.get(i);
                if (value.equals("order")) {
                    var date = list.get(++i);
                    vals.add(new Order(date));
                } else if (value.equals("item")) {
                    var qty = Integer.parseInt(list.get(++i));
                    var description = list.get(++i);
                    Order order = vals.get(vals.size() - 1);
                    order.addDetail(description, qty);
                }
            }
            vals.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return vals;
    }

    private static void addOrder(
        Connection conn,
        PreparedStatement psOrder,
        PreparedStatement psDetail,
        Order order
    ) throws SQLException {

        try {
            conn.setAutoCommit(false);
            int orderId = -1;
            psOrder.setString(1, order.dateString());
            if (psOrder.executeUpdate() == 1) {
                var rs = psOrder.getGeneratedKeys();
                if (rs.next()) {
                    orderId = rs.getInt(1);
                    System.out.println("orderId = " + order);

                    if (orderId > -1) {
                        psDetail.setInt(1, orderId);
                        for (OrderDetail od : order.details()) {
                            psDetail.setString(2, od.itemDescription());
                            psDetail.setInt(3, od.qty());
                            psDetail.addBatch();
                        }
                        int[] data = psDetail.executeBatch();
                        int rowsInserted = Arrays.stream(data).sum();
                        if (rowsInserted != order.details().size()) {
                            throw new SQLException("Inserts don't match");
                        }
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private static void addOrders(Connection conn, List<Order> orders) {
        try (
            PreparedStatement psOrder = conn.prepareStatement(INSERT_ORDER,
                Statement.RETURN_GENERATED_KEYS);
            PreparedStatement psDetail = conn.prepareStatement(INSERT_ORDER_DETAILS,
                Statement.RETURN_GENERATED_KEYS)
        ) {
            orders.forEach(o -> {
                try {
                    addOrder(conn, psOrder, psDetail, o);
                } catch (SQLException e) {
                    System.err.printf(
                        "%d (%s) %s%n",
                        e.getErrorCode(),
                        e.getSQLState(),
                        e.getMessage()
                    );
                    System.err.println("Problem: " + psOrder);
                    System.err.println("Order: " + o);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
