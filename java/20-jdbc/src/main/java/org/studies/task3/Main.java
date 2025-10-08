package org.studies.task3;

import static org.studies.lesson3.Main.setUpDataSource;
import static org.studies.task2.Main.readData;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        var dataSource = setUpDataSource("storefront");
        try (Connection connection = dataSource.getConnection(
            System.getenv("PG_USERNAME"),
            System.getenv("PG_PASSWORD")
        )) {
            var orders = readData();
            var formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withResolverStyle(
                ResolverStyle.STRICT);
            orders.forEach(order -> {
                var date = Timestamp.valueOf(LocalDateTime.parse(order.dateString(), formatter));
                var orderDetails = order.details()
                    .stream()
                    .map(detail -> "{ \"description\" : \"" + detail.itemDescription() + "\", \"qty\": " + detail.qty() + " }")
                    .collect(Collectors.joining(",", "[", "]"));
                callAddOrder(connection, date, orderDetails);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void callAddOrder(Connection connection, Timestamp order_date, String orderDetails) {
        CallableStatement cs;
        try {
            cs = connection.prepareCall(
                "CALL storefront.add_orders(?, ?::jsonb, ?, ?)");
            cs.setTimestamp(1, order_date);
            cs.setString(2, orderDetails);
            cs.setInt(3, 0);
            cs.setInt(4, 0);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.execute();
            System.out.printf("%d order details have been added for order id %s%n",
                cs.getInt(4), cs.getInt(3));

        } catch (SQLException e) {
            System.err.println(e.getErrorCode() + " " + e.getMessage());
        }
    }
}
