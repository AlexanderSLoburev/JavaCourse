package com.example.shop.validation;

import com.example.shop.model.Order;

/**
 * Rejects an order whose delivery address was never specified by the buyer.
 * Runs first in the chain, before stock and balance checks.
 */
public final class AddressValidator extends AbstractValidator {

  private static final String UNSPECIFIED = "not specified";
  private static final String CHECK = "Address";

  @Override
  protected void doValidate(Order order) {
    if (order.address().equals(UNSPECIFIED)) {
      throw new OrderRejectedException(
          CHECK + ": a delivery address must be specified before checkout");
    }
  }
}
