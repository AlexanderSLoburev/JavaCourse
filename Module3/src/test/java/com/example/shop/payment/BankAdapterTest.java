package com.example.shop.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Adapter pattern: BankAdapter adapts the incompatible LegacyBankSystem
 * (integer cents API) to the PaymentGateway abstraction.
 */
class BankAdapterTest {

    private static final String ACCOUNT = "BY00BANK123";

    @Test
    void convertsDollarsToIntegerCentsForLegacySettlement() {
        LegacyBankSystem legacy = new LegacyBankSystem();
        BankAdapter adapter = new BankAdapter(legacy, ACCOUNT);

        PaymentResult result = adapter.charge("O1", "Card", 123.45);

        assertEquals(ACCOUNT, legacy.lastAccount());
        assertEquals(12345, legacy.lastCents());
        assertEquals("O1: 12345 cents", result.reference());
    }

    @Test
    void roundsFractionsOfACent() {
        LegacyBankSystem legacy = new LegacyBankSystem();
        BankAdapter adapter = new BankAdapter(legacy, ACCOUNT);

        adapter.charge("O1", "Card", 1.234);

        assertTrue(legacy.lastCents() == 123
                       || legacy.lastCents() == 124,
                   "should round 1.234 to 123 or 124 cents, was "
                       + legacy.lastCents());
    }

    @Test
    void legacySystemIsUsedAsAPaymentGateway() {
        LegacyBankSystem legacy = new LegacyBankSystem();
        PaymentGateway gateway = new BankAdapter(legacy, ACCOUNT);

        PaymentResult result = gateway.charge("O2", "PayPal", 10.0);

        assertEquals("O2: 1000 cents", result.reference());
    }
}
