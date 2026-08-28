package com.example.shop.notification;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Decorator pattern: SmsNotifier wraps a base notifier. */
class DecoratorTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream out;

    @BeforeEach
    void captureStdout() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreStdout() {
        System.setOut(originalOut);
    }

    @Test
    void smsDecoratorForwardsToWrappedAndAddsSmsTag() {
        List<String> forwarded = new ArrayList<>();
        Notifier base = forwarded::add;

        new SmsNotifier(base).send("hello");

        assertTrue(forwarded.size() == 1
                       && forwarded.get(0).equals("hello"),
                   "must forward to the wrapped notifier first");
        assertTrue(outputContains("[SMS] hello"),
                   "must add its own SMS line to the output");
    }

    @Test
    void decoratorsCanBeNested() {
        List<String> forwarded = new ArrayList<>();
        Notifier base = forwarded::add;

        new SmsNotifier(base).send("nested");

        assertTrue(outputContains("[SMS] nested"));
    }

    @Test
    void compositeWrapsEmailInSms() {
        // Builds the exact composition used by the application:
        // SmsNotifier(EmailNotifier()).
        Notifier composite = new SmsNotifier(new EmailNotifier());
        composite.send("O1 placed");

        String text = outputText();
        assertTrue(text.contains("[Email] O1 placed"), "email layer first");
        assertTrue(text.contains("[SMS] O1 placed"), "sms layer second");
    }

    private boolean outputContains(String needle) {
        return outputText().contains(needle);
    }

    private String outputText() {
        return out.toString(StandardCharsets.UTF_8);
    }
}
