package com.pilshikov.homework2.rate_limited_printer;

public class RateLimitedPrinterTest {
    public static void main(String[] args) {
        RateLimitedPrinter limitedPrinter = new RateLimitedPrinter(3000);
        for (int i = 0; i < 1_000_000_000; i++) {
            limitedPrinter.print(String.valueOf(i));
        }
    }
}
