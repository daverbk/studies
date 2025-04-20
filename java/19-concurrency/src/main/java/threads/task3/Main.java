package threads.task3;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ShoeWarehouse {

    private final List<Order> orders;
    public final static String[] PRODUCTS_LIST = {
        "Nike Corsair",
        "Nike Air Max",
        "Nike Air Force 1"
    };
    private final ExecutorService fulfillmentService;

    public ShoeWarehouse() {
        this.orders = new LinkedList<>();
        fulfillmentService = Executors.newFixedThreadPool(3);
    }

    public void shutdown() {
        fulfillmentService.shutdown();
    }

    public synchronized void receiveOrder(Order order) {
        while (orders.size() > 20) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        orders.add(order);
        String threadName = Thread.currentThread().getName();
        System.out.printf("The order #%s has been accepted by thread %s\n", order.id(), threadName);
        fulfillmentService.submit(this::fulfillOrders);
        notifyAll();
    }

    public synchronized Order fulfillOrders() {
        while (orders.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String threadName = Thread.currentThread().getName();
        var order = orders.remove(0);
        System.out.printf("Order #%s is processed by %s.\n", order.id(), threadName);
        notifyAll();
        return order;
    }
}

record Order(String id, String item, int quantity) {

}

public class Main {

    private static final Random random = new Random();

    public static void main(String[] args) {
        ShoeWarehouse warehouse = new ShoeWarehouse();

        ExecutorService orderingService = Executors.newCachedThreadPool();

        Callable<Order> orderingTask = () -> {
            Order newOrder = generateOrder();
            try {
                Thread.sleep(random.nextInt(500, 5000));
                warehouse.receiveOrder(newOrder);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return newOrder;
        };

//        List<Callable<Order>> tasks = Collections.nCopies(15, orderingTask);
//        try {
//            orderingService.invokeAll(tasks);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        try {
            for (int i = 0; i < 15; i++) {
                Thread.sleep(random.nextInt(500, 2000));
                orderingService.submit(() -> warehouse.receiveOrder(generateOrder()));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        orderingService.shutdown();
        try {
            orderingService.awaitTermination(6, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        warehouse.shutdown();
    }

    private static Order generateOrder() {
        return new Order(
            UUID.randomUUID().toString(),
            ShoeWarehouse.PRODUCTS_LIST[random.nextInt(0, 3)],
            random.nextInt(1, 4)
        );
    }
}
