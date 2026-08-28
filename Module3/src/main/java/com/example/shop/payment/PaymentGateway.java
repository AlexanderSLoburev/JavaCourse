package com.example.shop.payment;

/**
 * Abstraction over the billing backend. Both the real gateway and the proxy
 * depend only on this interface.
 */
public interface PaymentGateway {

  /**
   * Charges the given amount for an order.
   *
   * @return a payment reference on success
   */
  PaymentResult charge(String orderId, String method, double amount);
}
