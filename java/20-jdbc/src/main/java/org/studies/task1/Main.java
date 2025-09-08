package org.studies.task1;

import static org.studies.lesson3.Main.setUpDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        var dataSource = setUpDataSource("storefront");
        try (Connection conn = dataSource.getConnection()) {
            var orderId = insertOrder(conn,
                List.of("Beautiful spring curtains", "Strong wooden table"));
            System.out.println("Inserted order with id: " + orderId);
            var isOrderDeleted = deleteOrder(conn, orderId);
            System.out.println("Order with id: " + orderId + " was deleted: " + isOrderDeleted);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int insertOrder(Connection conn, List<String> itemsDescriptions)
        throws SQLException {
        var insertOrder = "INSERT INTO storefront.order (order_date) VALUES (CURRENT_DATE)";
        var insertOrderDetails = "INSERT INTO storefront.order_details (item_description, order_id) VALUES ('%s', %d)";
        var resultOrderId = -1;

        try (Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            statement.execute(insertOrder, Statement.RETURN_GENERATED_KEYS);
            var rs = statement.getGeneratedKeys();
            int orderId = (rs != null && rs.next()) ? rs.getInt(1) : -1;
            for (String itemDescription : itemsDescriptions) {
                statement.execute(insertOrderDetails.formatted(itemDescription, orderId));
            }
            conn.commit();
            resultOrderId = orderId;
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }

        conn.setAutoCommit(true);
        return resultOrderId;
    }

    public static boolean deleteOrder(Connection conn, int orderId) throws SQLException {
        var deleteOrder = "DELETE FROM storefront.order WHERE order_id = %d";
        var result = false;
        try (Statement statement = conn.createStatement()) {
            statement.execute(deleteOrder.formatted(orderId));
            result = true;
        }
        return result;
    }
}
