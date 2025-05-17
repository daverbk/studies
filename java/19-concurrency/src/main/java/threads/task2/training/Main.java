package threads.task2.training;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
        System.out.printf("The order #%s has been accepted\n", order.id());
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
        String threadName = Thread.currentThread().getName();
        var order = orders.remove(0);
        System.out.printf("Order #%s is processed by %s.\n", order.id(), threadName);
        notifyAll();
        return order;
    }
}

class Main {

    public static void main(String[] args) {
        var warehouse = new Warehouse();

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 100; i++) {
                warehouse.receiveOrder(
                    new Order(UUID.randomUUID().toString(), "Mega Cool Shoe", i)
                );
            }
        });
        producer.start();

        for (int i = 0; i < 2; i++) {
            Thread consumer = new Thread(() -> {
                for (int j = 0; j < 50; j++) {
                    warehouse.fulfillOrder();
                }
            });
            consumer.start();
        }
    }
}
