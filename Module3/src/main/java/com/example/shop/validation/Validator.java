package com.example.shop.validation;

import com.example.shop.model.Order;



/**
 * A single step in the order-validation chain. New checks can be added
 * independently and chained without modifying existing ones (OCP).
 */
public interface Validator {

  /** Links the next validator in the chain, or {@code null} to end it. */
  void setNext(Validator next);

  /** Validates the order, forwarding to the next validator if it passes. */
  void validate(Order order);
}
