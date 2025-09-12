package org.studies.task2;

import static org.studies.lesson3.Main.setUpDataSource;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

record OrderDetail(int orderDetailId, String itemDescription, int qty) {

    public OrderDetail(String itemDescription, int qty) {
        this(-1, itemDescription, qty);
    }
}

record Order(int orderId, String dateString, List<OrderDetail> details) {

    public Order(String dateString) {
        this(-1, dateString, new ArrayList<>());
    }

    public void addDetail(String itemDescription, int qty) {
        OrderDetail item = new OrderDetail(itemDescription, qty);
        details.add(item);
    }
}

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

    private static List<Order> readData() {
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
                }
            }
            vals.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return vals;
    }
}
