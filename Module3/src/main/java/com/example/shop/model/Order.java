package com.example.shop.model;

import com.example.shop.util.Require;
import java.util.ArrayList;
import java.util.List;

/**
 * An immutable order built through the Order.Builder.
 */
public final class Order {

  private static final String DEFAULT_ADDRESS = "not specified";
  private static final int MIN_LINES = 1;

  private final String id;
  private final Customer customer;
  private final List<OrderLine> lines;
  private final String address;
  private final boolean express;

  private Order(Builder builder) {
    this.id = builder.id;
    this.customer = builder.customer;
    this.lines = List.copyOf(builder.lines);
    this.address = builder.address;
    this.express = builder.express;
  }

  public String id() { return id; }

  public Customer customer() { return customer; }

  public List<OrderLine> lines() { return lines; }

  public String address() { return address; }

  public boolean express() { return express; }

  /** Grand total of the order. */
  public double total() {
    return lines.stream().mapToDouble(OrderLine::subtotal).sum();
  }

  @Override
  public String toString() {
    return String.format("Order[id=%s, customer=%s, lines=%d, total=%.2f]", id,
                         customer.name(), lines.size(), total());
  }

  public static Builder builder(String id, Customer customer) {
    return new Builder(id, customer);
  }

  /** Fluent builder enforcing the Order invariants before build(). */
  public static final class Builder {

    private final String id;
    private final Customer customer;
    private final List<OrderLine> lines = new ArrayList<>();
    private String address = DEFAULT_ADDRESS;
    private boolean express;

    private Builder(String id, Customer customer) {
      this.id = Require.notBlank(id, "id");
      this.customer = Require.notNull(customer, "customer");
    }

    public Builder addLine(OrderLine line) {
      lines.add(Require.notNull(line, "line"));
      return this;
    }

    public Builder address(String address) {
      this.address = Require.notBlank(address, "address");
      return this;
    }

    public Builder express(boolean express) {
      this.express = express;
      return this;
    }

    public Order build() {
      if (lines.size() < MIN_LINES) {
        throw new IllegalStateException("an order must contain at least " +
                                        MIN_LINES + " line");
      }
      return new Order(this);
    }
  }
}
