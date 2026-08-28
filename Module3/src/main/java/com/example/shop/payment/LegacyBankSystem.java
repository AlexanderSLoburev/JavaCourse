package com.example.shop.payment;

import com.example.shop.util.Require;

/**
 * The company's old bank with an incompatible API: it works with integer cent
 * amounts and a fixed settlement account, and knows nothing about
 * PaymentGateway.
 */
public final class LegacyBankSystem {

  private String lastAccount;
  private int lastCents;

  /** Settles the given amount (in integer cents) for the given account. */
  public void settle(String account, int cents) {
    Require.notNull(account, "account");
    if (cents < 0) {
      throw new IllegalArgumentException("cents must be non-negative, was " +
                                         cents);
    }
    System.out.printf(String.format("$%f.2 was debited from the account %s",
                                    (double)cents / 100, account));
    this.lastAccount = account;
    this.lastCents = cents;
  }

  public String lastAccount() { return lastAccount; }

  public int lastCents() { return lastCents; }
}
