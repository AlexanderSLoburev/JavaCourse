package com.example.shop.model;

import com.example.shop.util.Require;

/** A single line of an order: a product and the ordered quantity. */
public record OrderLine(Product product, int quantity) {

  private static final int MIN_QUANTITY = 1;

  public OrderLine {
    Require.notNull(product, "product");
    if (quantity < MIN_QUANTITY) {
      throw new IllegalArgumentException("quantity must be >= " + MIN_QUANTITY +
                                         ", was " + quantity);
    }
  }

  /** Line total = unit price * quantity. */
  double subtotal() { return product.unitPrice() * quantity; }
}
