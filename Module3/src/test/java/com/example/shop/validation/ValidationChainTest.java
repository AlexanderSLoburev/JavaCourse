package com.example.shop.validation;

import static com.example.shop.TestData.customer;
import static com.example.shop.TestData.product;
import static com.example.shop.TestData.singleLineOrder;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.shop.model.Customer;
import com.example.shop.model.Order;
import com.example.shop.model.Product;
import org.junit.jupiter.api.Test;

/** Chain of Responsibility: the order-validation chain. */
class ValidationChainTest {

    private static final Customer ALICE =
        customer("c1", "Alice", 5000.0);
    private static final Product BOOK = product("B1", "Book", 30.0);

    /** Builds the full Address -> Stock -> Balance chain. */
    private static Validator fullChain() {
        AbstractValidator address = new AddressValidator();
        AbstractValidator stock = new StockValidator();
        AbstractValidator balance = new BalanceValidator();
        address.setNext(stock);
        stock.setNext(balance);
        return address;
    }

    @Test
    void orderPassingAllChecksIsNotRejected() {
        Order ok = singleLineOrder(ALICE, BOOK, 2, "Mira 1");
        assertDoesNotThrow(() -> fullChain().validate(ok));
    }

    @Test
    void chainStopsAtMissingAddress() {
        Order noAddress = singleLineOrder(ALICE, BOOK, 2);
        OrderRejectedException e =
            new OrderRejectedExpecter().expect(fullChain(), noAddress);
        assertTrue(e.getMessage().startsWith("Address:"));
    }

    @Test
    void chainContinuesToStockCheckWhenAddressPresent() {
        // Address ok but quantity over the max -> Stock rejects.
        Order overStocked = singleLineOrder(ALICE, BOOK, 250, "Mira 1");
        OrderRejectedException e =
            new OrderRejectedExpecter().expect(fullChain(), overStocked);
        assertTrue(e.getMessage().startsWith("Stock:"));
    }

    @Test
    void balanceIsCheckedAfterStockAndAddress() {
        Customer poor = customer("c2", "Bob", 20.0);
        Order unaffordable = singleLineOrder(poor, BOOK, 2, "Mira 1");
        OrderRejectedException e =
            new OrderRejectedExpecter().expect(fullChain(), unaffordable);
        assertTrue(e.getMessage().startsWith("Balance:"));
    }
}

/** Helper that validates and returns the rejection exception if thrown. */
final class OrderRejectedExpecter {
    OrderRejectedException expect(Validator validator, Order order) {
        try {
            validator.validate(order);
        } catch (OrderRejectedException e) {
            return e;
        }
        throw new AssertionError("expected order to be rejected");
    }
}
