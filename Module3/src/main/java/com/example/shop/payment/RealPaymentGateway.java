package com.example.shop.payment;

import com.example.shop.util.Require;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The real, acquirer-side billing backend. It is deliberately treated as
 * "expensive", which is why caching the result matters.
 */
public final class RealPaymentGateway implements PaymentGateway {

  private static final String REF_PREFIX = "ACQ-";

  private final AtomicLong counter = new AtomicLong(0);

  @Override
  public PaymentResult charge(String orderId, String method, double amount) {
    Require.notBlank(orderId, "orderId");
    long n = counter.incrementAndGet();
    System.out.printf("[Acquirer] Charged %.2f by %s for %s -> %s%n", amount,
                      method, orderId, REF_PREFIX + n);
    return new PaymentResult(REF_PREFIX + n);
  }
}
