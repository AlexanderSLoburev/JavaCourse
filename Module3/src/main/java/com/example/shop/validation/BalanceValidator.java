package com.example.shop.validation;

import com.example.shop.model.Order;

/**
 * Rejects an order when the customer's declared balance is below the total.
 */
public final class BalanceValidator extends AbstractValidator {

  private static final String CHECK = "Balance";

  @Override
  protected void doValidate(Order order) {
    double total = order.total();
    double available = order.customer().accountBalance();
    if (Double.compare(available, total) < 0) {
      throw new OrderRejectedException(CHECK + ": available " +
                                       format(available) + " < total " +
                                       format(total));
    }
  }

  private static String format(double value) { return "%.2f".formatted(value); }
}
