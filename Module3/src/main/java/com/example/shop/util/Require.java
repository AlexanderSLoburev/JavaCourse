package com.example.shop.util;

import java.util.Objects;

/**
 * Small validation helpers shared across the application. Keeps the
 * null-safety / blank-checking boilerplate in one place (DRY).
 */
public final class Require {

  private Require() { throw new AssertionError("not instantiable"); }

  public static <T> T notNull(T value, String name) {
    return Objects.requireNonNull(value, name + " must not be null");
  }

  public static String notBlank(String value, String name) {
    String checked = notNull(value, name);
    if (checked.isBlank()) {
      throw new IllegalArgumentException(name + " must not be blank");
    }
    return checked;
  }
}
