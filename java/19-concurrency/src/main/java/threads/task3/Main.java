package threads.task3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class ShoeWarehouse {

    private final List<Order> orders;
    public final static String[] PRODUCTS_LIST = {
        "Nike Corsair",
        "Nike Air Max",
        "Nike Air Force 1"
    };
    private static ExecutorService worker;

    public ShoeWarehouse() {
        this.orders = new LinkedList<>();
        worker = Executors.newFixedThreadPool(3);
    }

    public synchronized void receiveOrder(Order order) {
        while (orders.size() > 20) {
            try {
                System.out.println("Too many orders, waiting for the warehouse to be freed");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        orders.add(order);
        System.out.printf("The order #%s has been accepted\n", order.id());
        notifyAll();
    }

    public synchronized List<Future<Order>> fulfillOrders() {
        List<Future<Order>> processed = new ArrayList<>();
        while (!orders.isEmpty()) {
            processed.add(
                worker.submit(
                    () -> {
                        String threadName = Thread.currentThread().getName();
                        var order = orders.remove(0);
                        System.out.printf("Order #%s is processed by %s.\n", order.id(),
                            threadName);
                        return order;
                    }
                )
            );
        }
        return processed;
    }
}


record Order(String id, String item, int quantity) {

}

public class Main {

    private static final Random random = new Random();

    public static void main(String[] args) {
        ShoeWarehouse warehouse = new ShoeWarehouse();

        var singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(
            () -> {
                for (int i = 1; i <= 15; i++) {
                    warehouse.receiveOrder(
                        new Order(
                            UUID.randomUUID().toString(),
                            ShoeWarehouse.PRODUCTS_LIST[random.nextInt(0, 3)],
                            random.nextInt(1, 4)
                        )
                    );
                }
            }
        );

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        warehouse.fulfillOrders();

        singleThreadExecutor.shutdown();
    }
}
