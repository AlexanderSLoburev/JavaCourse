package com.example.shop.validation;

import com.example.shop.model.Order;
import com.example.shop.util.Require;

/**
 * Runs a single concrete check and then
 * forwards to the next validator in the chain.
 */
public abstract class AbstractValidator implements Validator {

  private Validator next;

  @Override
  public void setNext(Validator next) {
    this.next = next;
  }

  @Override
  public final void validate(Order order) {
    Require.notNull(order, "order");
    doValidate(order);
    if (next != null) {
      next.validate(order);
    }
  }

  /** Performs this validator's single check. */
  protected abstract void doValidate(Order order);
}
