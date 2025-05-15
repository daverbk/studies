package threads.task2.training;

import java.util.LinkedList;
import java.util.List;

record Order(long id, String shoeType, int quantity) {

}

class Warehouse {

    private final List<Order> orders = new LinkedList<>();

    public synchronized void receiveOrder(Order order) {
        while (orders.size() >= 20) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        orders.add(order);
        System.out.println(
            "Thread " + Thread.currentThread().getName() + " has received order " + order.id()
        );
        notifyAll();
    }

    public synchronized void fulfillOrder() {
        while (orders.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        var processedOrder = orders.remove(0);
        System.out.println(
            "Thread " + Thread.currentThread().getName() + " has processed order No "
                + processedOrder.id()
        );
        notifyAll();
    }
}

class Main {

    public static void main(String[] args) {
        var warehouse = new Warehouse();

        var producerThread = new Thread(() -> {
            for (int i = 1; i <= 50; i++) {
                warehouse.receiveOrder(new Order(100000 + i, "Mega Cool Shoe", i));
            }
        });
        producerThread.start();

        for (int i = 0; i < 2; i++) {
            var consumer = new Thread(() -> {
                for (int j = 0; j < 25; j++) {
                    warehouse.fulfillOrder();
                }
            });
            consumer.start();
        }
    }
}
