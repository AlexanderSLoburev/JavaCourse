package com.example.shop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Builder pattern: Order.Builder. */
class OrderBuilderTest {

    private final Customer alice = new Customer("c1", "Alice", 5000.0);

    @Test
    void buildsOrderFromFluentBuilder() {
        Product book = new Product("B1", "Book", 30.0);
        Order order = Order.builder("O1", alice)
                           .addLine(new OrderLine(book, 2))
                           .address("Mira 1")
                           .express(true)
                           .build();

        assertEquals("O1", order.id());
        assertEquals(alice, order.customer());
        assertEquals(1, order.lines().size());
        assertEquals("Mira 1", order.address());
        assertTrue(order.express());
    }

    @Test
    void totalSumsLineSubtotals() {
        Order order = Order.builder("O1", alice)
                           .addLine(new OrderLine(product("P1", "A", 10.0), 2))
                           .addLine(new OrderLine(product("P2", "B", 5.0), 3))
                           .build();

        assertEquals(35.0, order.total(), 1e-9);
    }

    @Test
    void defaultAddressIsMarkedAsUnspecified() {
        Order order = Order.builder("O1", alice)
                           .addLine(new OrderLine(product("P1", "A", 1.0), 1))
                           .build();

        assertEquals("not specified", order.address());
        assertFalse(order.express());
    }

    @Test
    void linesAreDefensivelyCopied() {
        Order order = Order.builder("O1", alice)
                           .addLine(new OrderLine(product("P1", "A", 1.0), 1))
                           .build();
        assertThrows(UnsupportedOperationException.class,
                     () -> order.lines().add(new OrderLine(product("P2", "B", 1.0), 1)));
    }

    @Test
    void buildRejectsOrderWithoutLines() {
        assertThrows(IllegalStateException.class,
                     () -> Order.builder("O1", alice).build());
    }

    @Test
    void builderRequiresNonNullCustomer() {
        assertThrows(NullPointerException.class,
                     () -> Order.builder("O1", null));
    }

    @Test
    void builderRejectsBlankId() {
        assertThrows(IllegalArgumentException.class,
                     () -> Order.builder("   ", alice));
    }

    @Test
    void builderRejectsNullLine() {
        assertThrows(NullPointerException.class,
                     () -> Order.builder("O1", alice).addLine(null));
    }

    private static Product product(String sku, String name, double price) {
        return new Product(sku, name, price);
    }
}
