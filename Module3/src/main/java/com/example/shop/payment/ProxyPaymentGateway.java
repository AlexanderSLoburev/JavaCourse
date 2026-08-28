package com.example.shop.payment;

import com.example.shop.util.Require;
import java.util.HashMap;
import java.util.Map;

/**
 * A caching proxy over the payment gateway: repeated charges for the same order
 * are served from cache to avoid hitting the expensive acquirer again.
 */
public final class ProxyPaymentGateway implements PaymentGateway {

  private final PaymentGateway delegate;
  private final Map<String, PaymentResult> cache = new HashMap<>();

  public ProxyPaymentGateway(PaymentGateway delegate) {
    this.delegate = Require.notNull(delegate, "delegate");
  }

  @Override
  public PaymentResult charge(String orderId, String method, double amount) {
    Require.notBlank(orderId, "orderId");
    return cache.computeIfAbsent(orderId, id -> {
      System.out.println("[Proxy] Cache miss for " + id +
                         ", charging acquirer...");
      return delegate.charge(id, method, amount);
    });
  }

  public int cachedOrderCount() { return cache.size(); }
}
