package com.example.shop.payment;

import com.example.shop.util.Require;

/** Payment strategy that bills a credit card. */
public final class CardPayment implements PaymentStrategy {

  private static final String DISPLAY_NAME = "Card";
  private static final int MASK_LENGTH = 4;
  private static final String MASK_PREFIX = "*";

  private final String cardNumber;

  public CardPayment(String cardNumber) {
    this.cardNumber = Require.notNull(cardNumber, "cardNumber");
    if (cardNumber.length() < MASK_LENGTH) {
      throw new IllegalArgumentException("cardNumber must be at least " +
                                         MASK_LENGTH + " digits");
    }
  }

  @Override
  public String pay(PaymentGateway gateway, String orderId, double amount) {
    PaymentResult result = gateway.charge(orderId, name(), amount);
    return "card " + MASK_PREFIX + lastDigits() + " -> " + result.reference();
  }

  @Override
  public String name() {
    return DISPLAY_NAME;
  }

  private String lastDigits() {
    return cardNumber.substring(cardNumber.length() - MASK_LENGTH);
  }
}
