package com.example.shop.model;

import com.example.shop.util.Require;

/** A customer of the shop. */
public record Customer(String id, String name, double accountBalance) {

  public Customer {
    Require.notBlank(id, "id");
    Require.notBlank(name, "name");
  }
}
