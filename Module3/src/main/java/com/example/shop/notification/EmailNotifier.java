package com.example.shop.notification;

import com.example.shop.util.Require;



/** Base notifier that delivers the message by e-mail. */
public final class EmailNotifier implements Notifier {

  private static final String TAG = "[Email] ";

  @Override
  public void send(String message) {
    Require.notNull(message, "message");
    System.out.println(TAG + message);
  }
}
