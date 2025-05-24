package threads.task3.training;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

record Order(String id, String shoeType, int quantity) {

}

class Warehouse {

    private final List<Order> orders;

    Warehouse() {
        this.orders = new LinkedList<>();
    }

    public synchronized void receiveOrder(Order order) {
        while (orders.size() > 10) {
            try {
                System.out.println("Too many orders, waiting for the warehouse to be freed");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        orders.add(order);
        System.out.printf(
            "The order #%s has been accepted by thread %s\n",
            order.id(),
            Thread.currentThread().getName()
        );
        notifyAll();
    }

    public synchronized Order fulfillOrder() {
        while (orders.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        var order = orders.remove(0);
        System.out.printf(
            "Order #%s is processed by %s\n",
            order.id(),
            Thread.currentThread().getName()
        );
        notifyAll();
        return order;
    }
}

class CustomNameThreadFactory implements ThreadFactory {

    private final String customNamePart;
    private final Random random;

    public CustomNameThreadFactory(String customNamePart) {
        this.customNamePart = customNamePart;
        this.random = new Random();
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, customNamePart + '-' + random.nextInt(1, 1000));
    }
}

class Main {

    public static void main(String[] args) {
        var warehouse = new Warehouse();

        ExecutorService producers = Executors.newCachedThreadPool(
            new CustomNameThreadFactory("producer")
        );

        for (int j = 1; j <= 50; j++) {
            int finalJ = j;
            producers.submit(() -> warehouse.receiveOrder(
                new Order(UUID.randomUUID().toString(), "Mega Cool Shoe", finalJ)
            ));
        }

        ExecutorService consumers = Executors.newFixedThreadPool(
            2,
            new CustomNameThreadFactory("consumer")
        );

        for (int j = 0; j < 50; j++) {
            consumers.submit(
                () -> {
                    warehouse.fulfillOrder();
                }
            );
        }

        producers.shutdown();
        consumers.shutdown();
    }
}
