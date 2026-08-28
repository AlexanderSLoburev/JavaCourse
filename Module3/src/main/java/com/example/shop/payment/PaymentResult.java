package com.example.shop.payment;

import com.example.shop.util.Require;



/** Outcome of a successful charge. */
public record PaymentResult(String reference) {

  public PaymentResult { Require.notBlank(reference, "reference"); }
}
