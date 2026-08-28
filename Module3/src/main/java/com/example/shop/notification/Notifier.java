package com.example.shop.notification;

/** Abstraction for delivering a message to a customer. */
public interface Notifier {

  /** Sends the given message through the underlying channel(s). */
  void send(String message);
}
