package com.example.shop.checkout;

import static com.example.shop.TestData.customer;
import static com.example.shop.TestData.product;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.shop.model.Customer;
import com.example.shop.model.Order;
import com.example.shop.model.Product;
import com.example.shop.notification.Notifier;
import com.example.shop.payment.BankAdapter;
import com.example.shop.payment.LegacyBankSystem;
import com.example.shop.payment.PaymentContext;
import com.example.shop.payment.ProxyPaymentGateway;
import com.example.shop.validation.AbstractValidator;
import com.example.shop.validation.AddressValidator;
import com.example.shop.validation.BalanceValidator;
import com.example.shop.validation.StockValidator;
import com.example.shop.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Integration: wires the whole pipeline together - validation chain, payment
 * strategy, proxy over adapter, and a notifier - as the application does.
 */
class CheckoutServiceTest {

    private static final String ACCOUNT = "BY00BANK123";

    @Test
    void validOrderIsPlacedThroughTheFullPipeline() {
        List<String> notifications = new ArrayList<>();
        CheckoutService checkout = buildCheckout(notifications);

        Customer alice = customer("c1", "Alice", 5000.0);
        Product book = product("B1", "Book", 30.0);
        Order order = Order.builder("O1", alice)
                           .addLine(new com.example.shop.model.OrderLine(book, 2))
                           .address("Mira 1")
                           .build();

        boolean placed = checkout.place(order);

        assertTrue(placed);
        assertTrue(notifications.stream().anyMatch(
            n -> n.contains("placed") && n.contains("O1")));
    }

    @Test
    void orderRejectedByValidationIsNotCharged() {
        List<String> notifications = new ArrayList<>();
        CheckoutService checkout = buildCheckout(notifications);

        Customer poor = customer("c2", "Bob", 20.0);
        Product book = product("B1", "Book", 30.0);
        Order order = Order.builder("O1", poor)
                           .addLine(new com.example.shop.model.OrderLine(book, 2))
                           .address("Mira 1")
                           .build();

        boolean placed = checkout.place(order);

        assertFalse(placed);
        assertTrue(notifications.stream().anyMatch(
            n -> n.contains("rejected") && n.contains("Balance")));
    }

    private static CheckoutService buildCheckout(List<String> notifications) {
        Notifier notifier = notifications::add;
        return new CheckoutService(
            buildValidatorChain(),
            new PaymentContext(new com.example.shop.payment.CardPayment("4111111111111111")),
            new ProxyPaymentGateway(
                new BankAdapter(new LegacyBankSystem(), ACCOUNT)),
            notifier);
    }

    private static Validator buildValidatorChain() {
        AbstractValidator address = new AddressValidator();
        AbstractValidator stock = new StockValidator();
        AbstractValidator balance = new BalanceValidator();
        address.setNext(stock);
        stock.setNext(balance);
        return address;
    }
}
