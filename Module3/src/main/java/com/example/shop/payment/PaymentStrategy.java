package com.example.shop.payment;

/**
 * A payment strategy: the billing logic for a specific payment method.
 */
public interface PaymentStrategy {

  /** Executes the payment via the gateway and returns a receipt description. */
  String pay(PaymentGateway gateway, String orderId, double amount);

  /** Display name of the method (used as the payment channel label). */
  String name();
}
