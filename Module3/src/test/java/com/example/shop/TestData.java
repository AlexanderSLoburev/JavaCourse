package com.example.shop;

import com.example.shop.model.Customer;
import com.example.shop.model.Order;
import com.example.shop.model.OrderLine;
import com.example.shop.model.Product;

/** Small factory to keep tests concise. */
public final class TestData {

    private TestData() {}

    public static Customer customer(String id, String name, double balance) {
        return new Customer(id, name, balance);
    }

    public static Product product(String sku, String name, double price) {
        return new Product(sku, name, price);
    }

    public static OrderLine line(Product product, int quantity) {
        return new OrderLine(product, quantity);
    }

    /** Order with a single line and no address (fails AddressValidator). */
    public static Order singleLineOrder(Customer customer, Product product,
                                        int quantity) {
        return Order.builder("ORD-" + product.sku(), customer)
                    .addLine(line(product, quantity))
                    .build();
    }

    /** Order with a delivery address, single line. */
    public static Order singleLineOrder(Customer customer, Product product,
                                        int quantity, String address) {
        return Order.builder("ORD-" + product.sku(), customer)
                    .addLine(line(product, quantity))
                    .address(address)
                    .build();
    }
}
