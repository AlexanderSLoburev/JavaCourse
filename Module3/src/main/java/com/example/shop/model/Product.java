package com.example.shop.model;

import com.example.shop.util.Require;

/** A purchasable item in the catalog. */
public record Product(String sku, String name, double unitPrice) {

  public Product {
    Require.notBlank(sku, "sku");
    Require.notBlank(name, "name");
    if (unitPrice < 0) {
      throw new IllegalArgumentException(
          "unitPrice must be non-negative, was " + unitPrice);
    }
  }
}
