package com.example.shop.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/** Strategy pattern: PaymentContext + swappable PaymentStrategy. */
class PaymentStrategyTest {

    private static final String CARD = "4111111111111111";

    private final PaymentGateway gateway = new RecordingGateway();

    @Test
    void contextDelegatesToCurrentStrategy() {
        PaymentContext context = new PaymentContext(new CardPayment(CARD));

        String receipt = context.execute(gateway, "O1", 120.0);

        assertEquals("card *1111 -> ref-O1", receipt);
    }

    @Test
    void strategyCanBeSwappedAtRuntime() {
        PaymentContext context =
            new PaymentContext(new CardPayment(CARD));
        PayPalPayment paypal = new PayPalPayment("a@b.com");

        context.setStrategy(paypal);

        assertSame(paypal, context.currentStrategy());
        String receipt = context.execute(gateway, "O1", 120.0);
        assertEquals("paypal a@b.com -> ref-O1", receipt);
    }

    @Test
    void paypalStrategyChargesWithItsName() {
        String receipt = new PayPalPayment("a@b.com")
                             .pay(gateway, "O1", 50.0);
        assertEquals("paypal a@b.com -> ref-O1", receipt);
        assertEquals("PayPal", new PayPalPayment("a@b.com").name());
        assertEquals("Card", new CardPayment(CARD).name());
    }

    /** Fake gateway returning a deterministic reference, no real billing. */
    private static final class RecordingGateway implements PaymentGateway {
        @Override
        public PaymentResult charge(String orderId, String method,
                                    double amount) {
            return new PaymentResult("ref-" + orderId);
        }
    }
}
