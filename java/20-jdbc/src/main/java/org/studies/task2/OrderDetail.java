package org.studies.task2;

public record OrderDetail(int orderDetailId, String itemDescription, int qty) {

    public OrderDetail(String itemDescription, int qty) {
        this(-1, itemDescription, qty);
    }
}
