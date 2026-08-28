package com.example.shop.payment;

import com.example.shop.util.Require;



/** Payment strategy that bills a PayPal account. */
public final class PayPalPayment implements PaymentStrategy {

  private static final String DISPLAY_NAME = "PayPal";

  private final String email;

  public PayPalPayment(String email) {
    this.email = Require.notBlank(email, "email");
  }

  @Override
  public String pay(PaymentGateway gateway, String orderId, double amount) {
    PaymentResult result = gateway.charge(orderId, name(), amount);
    return "paypal " + email + " -> " + result.reference();
  }

  @Override
  public String name() {
    return DISPLAY_NAME;
  }
}
