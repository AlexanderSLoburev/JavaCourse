package com.example.shop.validation;



/** Thrown when an order fails a validation step; the order is rejected. */
public final class OrderRejectedException extends RuntimeException {

  public OrderRejectedException(String message) { super(message); }
}
