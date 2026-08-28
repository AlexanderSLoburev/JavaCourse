package com.example.shop.payment;

import com.example.shop.util.Require;

/**
 * Holds the active PaymentStrategy and delegates payment to it.
 * The strategy can be swapped at runtime (e.g. the customer changes the method)
 * without modifying the holder itself.
 */
public final class PaymentContext {

  private PaymentStrategy strategy;

  public PaymentContext(PaymentStrategy strategy) {
    this.strategy = Require.notNull(strategy, "strategy");
  }

  public void setStrategy(PaymentStrategy strategy) {
    this.strategy = Require.notNull(strategy, "strategy");
  }

  public PaymentStrategy currentStrategy() { return strategy; }

  public String execute(PaymentGateway gateway, String orderId, double amount) {
    Require.notNull(gateway, "gateway");
    Require.notNull(orderId, "orderId");
    return strategy.pay(gateway, orderId, amount);
  }
}
