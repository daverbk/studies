package org.studies.task2;

import java.util.ArrayList;
import java.util.List;

public record Order(int orderId, String dateString, List<OrderDetail> details) {

    public Order(String dateString) {
        this(-1, dateString, new ArrayList<>());
    }

    public void addDetail(String itemDescription, int qty) {
        OrderDetail item = new OrderDetail(itemDescription, qty);
        details.add(item);
    }
}
