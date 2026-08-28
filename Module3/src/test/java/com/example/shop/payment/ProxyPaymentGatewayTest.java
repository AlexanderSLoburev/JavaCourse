package com.example.shop.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

/** Proxy pattern: ProxyPaymentGateway caches charges per order id. */
class ProxyPaymentGatewayTest {

    @Test
    void repeatedOrdersAreServedFromCache() {
        AtomicInteger charges = new AtomicInteger();
        PaymentGateway real = new RecordingGateway(charges);
        ProxyPaymentGateway proxy = new ProxyPaymentGateway(real);

        PaymentResult first = proxy.charge("O1", "Card", 100.0);
        PaymentResult second = proxy.charge("O1", "Card", 100.0);
        PaymentResult third = proxy.charge("O1", "Card", 100.0);

        assertSame(first, second);
        assertSame(first, third);
        assertEquals(1, charges.get());
        assertEquals(1, proxy.cachedOrderCount());
    }

    @Test
    void differentOrdersAreChargedSeparately() {
        AtomicInteger charges = new AtomicInteger();
        ProxyPaymentGateway proxy =
            new ProxyPaymentGateway(new RecordingGateway(charges));

        proxy.charge("O1", "Card", 10.0);
        proxy.charge("O2", "Card", 20.0);
        proxy.charge("O1", "Card", 30.0);

        assertEquals(2, charges.get());
        assertEquals(2, proxy.cachedOrderCount());
    }

    /** Counts how many times it actually charges the acquirer. */
    private static final class RecordingGateway implements PaymentGateway {
        private final AtomicInteger charges;

        RecordingGateway(AtomicInteger charges) { this.charges = charges; }

        @Override
        public PaymentResult charge(String orderId, String method,
                                    double amount) {
            charges.incrementAndGet();
            return new PaymentResult("ref-" + orderId);
        }
    }
}
