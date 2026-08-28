package com.example.shop.validation;

import com.example.shop.model.Order;

/** Rejects an order when any line exceeds the maximum orderable quantity. */
public final class StockValidator extends AbstractValidator {

  private static final int MAX_QUANTITY_PER_LINE = 100;
  private static final String CHECK = "Stock";

  @Override
  protected void doValidate(Order order) {
    boolean overStocked = order.lines().stream().anyMatch(
        line -> line.quantity() > MAX_QUANTITY_PER_LINE);
    if (overStocked) {
      throw new OrderRejectedException(
          CHECK + ": a line exceeds the max quantity of " +
          MAX_QUANTITY_PER_LINE);
    }
  }
}
