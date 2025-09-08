package org.studies.lesson3;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class Main {

    private static String CHECK_DATABASE =
        "SELECT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'storefront')";

    public static void main(String[] args) {

        var postgresDataSource = setUpDataSource("postgres");

        try (Connection conn = postgresDataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            System.out.println(metaData.getSQLStateType());
            if (!checkSchema(conn)) {
                System.out.println("storefront schema does not exist");
                setUpDatabase(conn);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean checkSchema(Connection conn) throws SQLException {
        var isDbPresent = false;
        try (Statement statement = conn.createStatement()) {
            var result = statement.executeQuery(CHECK_DATABASE);
            result.next();
            isDbPresent = result.getBoolean("exists");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            System.out.println("DB vendor: " + conn.getMetaData().getDatabaseProductName());
            return false;
        }
        return isDbPresent;
    }

    private static void setUpDatabase(Connection postgresConnection) throws SQLException {
        String createDatabase = "CREATE DATABASE storefront";
        String createSchema = "CREATE SCHEMA storefront";
        String createOrder = """
            CREATE TABLE storefront.order (
            order_id SERIAL NOT NULL PRIMARY KEY,
            order_date date NOT NULL
            )""";

        String createOrderDetails = """
            CREATE TABLE storefront.order_details (
            order_detail_id SERIAL NOT NULL PRIMARY KEY,
            item_description text,
            order_id INTEGER DEFAULT NULL,
            CONSTRAINT fk_order_id
            FOREIGN KEY (order_id)
            REFERENCES storefront.order (order_id)
            ON DELETE CASCADE
            )""";

        try (Statement postgresStatement = postgresConnection.createStatement()) {

            postgresStatement.execute(createDatabase);
            if (checkSchema(postgresConnection)) {
                System.out.println("Creating storefront database");
                var storefrontDataSource = setUpDataSource("storefront");
                try (
                    var storefrontConnection = storefrontDataSource.getConnection();
                    var storefrontStatement = storefrontConnection.createStatement()
                ) {
                    System.out.println("Creating storefront schema");
                    storefrontStatement.execute(createSchema);
                    storefrontStatement.execute(createOrder);
                    System.out.println("Successfully created Order");
                    storefrontStatement.execute(createOrderDetails);
                    System.out.println("Successfully created Order Details");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DataSource setUpDataSource(String database) {
        var dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{"localhost"});
        dataSource.setDatabaseName(database);
        dataSource.setPortNumbers(new int[]{5432});
        dataSource.setUser(System.getenv("PG_USERNAME"));
        dataSource.setPassword(System.getenv("PG_PASSWORD"));
        return dataSource;
    }
}
