package com.example.shop.checkout;

import com.example.shop.model.Order;
import com.example.shop.notification.Notifier;
import com.example.shop.payment.PaymentContext;
import com.example.shop.payment.PaymentGateway;
import com.example.shop.validation.OrderRejectedException;
import com.example.shop.validation.Validator;
import java.util.Objects;

/**
 * The checkout use case: builds an Order, validates it, charges the customer
 * and notifies the customer.
 */
public final class CheckoutService {

  private final Validator validator;
  private final PaymentContext payment;
  private final PaymentGateway gateway;
  private final Notifier notifier;

  public CheckoutService(Validator validator, PaymentContext payment,
                         PaymentGateway gateway, Notifier notifier) {
    this.validator = Objects.requireNonNull(validator, "validator");
    this.payment = Objects.requireNonNull(payment, "payment");
    this.gateway = Objects.requireNonNull(gateway, "gateway");
    this.notifier = Objects.requireNonNull(notifier, "notifier");
  }

  /**
   * Places the order.
   * @return true on success, false if the order was rejected by validation.
   */
  public boolean place(Order order) {
    Objects.requireNonNull(order, "order");
    try {
      validator.validate(order);
    } catch (OrderRejectedException e) {
      notifier.send("Order for " + order.customer().name() +
                    " rejected: " + e.getMessage());
      return false;
    }
    String receipt = payment.execute(gateway, order.id(), order.total());
    notifier.send("Order " + order.id() + " for " + order.customer().name() +
                  " placed, total " + format(order.total()) +
                  ". Payment: " + receipt);
    return true;
  }

  private static String format(double value) { return "%.2f".formatted(value); }
}
