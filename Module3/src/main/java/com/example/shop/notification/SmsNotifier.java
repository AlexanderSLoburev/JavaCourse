package com.example.shop.notification;

import com.example.shop.util.Require;



/**
 * A decorator that adds an SMS channel on top of an existing notifier, without
 * modifying the wrapped notifier (OCP).
 */
public final class SmsNotifier implements Notifier {

  private static final String TAG = "[SMS] ";

  private final Notifier wrapped;

  public SmsNotifier(Notifier wrapped) {
    this.wrapped = Require.notNull(wrapped, "wrapped");
  }

  @Override
  public void send(String message) {
    Require.notNull(message, "message");
    wrapped.send(message);
    System.out.println(TAG + message);
  }
}
