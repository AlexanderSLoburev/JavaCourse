package com.example.shop.app;

import com.example.shop.checkout.CheckoutService;
import com.example.shop.model.Customer;
import com.example.shop.model.Order;
import com.example.shop.model.OrderLine;
import com.example.shop.model.Product;
import com.example.shop.notification.EmailNotifier;
import com.example.shop.notification.SmsNotifier;
import com.example.shop.payment.BankAdapter;
import com.example.shop.payment.CardPayment;
import com.example.shop.payment.LegacyBankSystem;
import com.example.shop.payment.PaymentContext;
import com.example.shop.payment.ProxyPaymentGateway;
import com.example.shop.validation.AddressValidator;
import com.example.shop.validation.BalanceValidator;
import com.example.shop.validation.StockValidator;
import com.example.shop.validation.Validator;

public final class ShopApplication {

  private static final String SETTLEMENT_ACCOUNT = "BY00BANK123";
  private static final String CARD_NUMBER = "4111111111111111";

  public void run() {
    CheckoutService checkout = new CheckoutService(
        buildValidator(), new PaymentContext(new CardPayment(CARD_NUMBER)),
        new ProxyPaymentGateway(
            new BankAdapter(new LegacyBankSystem(), SETTLEMENT_ACCOUNT)),
        new SmsNotifier(new EmailNotifier()));

    Customer alice = new Customer("cust-1", "Alice", 5000.00);
    Customer bob = new Customer("cust-2", "Bob", 20.00);

    place(checkout, alice, new Product("BOOK-1", "Java Course Book", 30.00), 2);
    place(checkout, alice,
          new Product("DEVICE-1", "Mechanical Keyboard", 120.00), 1);
    place(checkout, bob, new Product("ACCESSORY-1", "USB-C Hub", 50.00), 1);
    place(checkout, alice, new Product("BULK-1", "Pack of Pens", 5.00), 250);
    placeWithoutAddress(checkout, alice,
                        new Product("BOOK-2", "Java Book 2", 25.00));
  }

  private static void placeWithoutAddress(CheckoutService checkout,
                                          Customer customer, Product product) {
    Order order = Order.builder("ORD-" + product.sku(), customer)
                      .addLine(new OrderLine(product, 1))
                      .build();
    boolean placed = checkout.place(order);
    System.out.println("  -> " + (placed ? "placed" : "rejected"));
  }

  private static void place(CheckoutService checkout, Customer customer,
                            Product product, int quantity) {
    Order order = Order.builder("ORD-" + product.sku(), customer)
                      .addLine(new OrderLine(product, quantity))
                      .address("Nizhny Novgorod, Mira 1")
                      .build();
    boolean placed = checkout.place(order);
    System.out.println("  -> " + (placed ? "placed" : "rejected"));
  }

  private static Validator buildValidator() {
    Validator address = new AddressValidator();
    Validator stock = new StockValidator();
    Validator balance = new BalanceValidator();
    address.setNext(stock);
    stock.setNext(balance);
    return address;
  }
}
