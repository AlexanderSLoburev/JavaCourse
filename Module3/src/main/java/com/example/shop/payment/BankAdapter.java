package com.example.shop.payment;

import com.example.shop.util.Require;

/**
 * Adapts the incompatible LegacyBankSystem so it can be used as a
 * PaymentGateway. It converts a dollar amount into integer cents
 * for the legacy settlement call.
 */
public final class BankAdapter implements PaymentGateway {

  public static final int CENTS_SCALE = 100;

  private final LegacyBankSystem legacy;
  private final String settlementAccount;

  public BankAdapter(LegacyBankSystem legacy, String settlementAccount) {
    this.legacy = Require.notNull(legacy, "legacy");
    this.settlementAccount =
        Require.notBlank(settlementAccount, "settlementAccount");
  }

  @Override
  public PaymentResult charge(String orderId, String method, double amount) {
    Require.notBlank(orderId, "orderId");
    int cents = toCents(amount);
    legacy.settle(settlementAccount, cents);
    System.out.printf(
        "[Adapter] Settled %.2f (%d cents) for %s via legacy bank%n", amount,
        cents, orderId);
    return new PaymentResult(orderId + ": " + cents + " cents");
  }

  private static int toCents(double amount) {
    return (int)Math.round(amount * CENTS_SCALE);
  }
}
