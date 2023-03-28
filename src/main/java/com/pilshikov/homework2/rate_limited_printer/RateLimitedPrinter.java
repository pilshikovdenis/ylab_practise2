package com.pilshikov.homework2.rate_limited_printer;

public class RateLimitedPrinter {
    private int interval;
    private long lastCall = 0;
    public RateLimitedPrinter(int interval) {
        this.interval = interval;
    }

    public void print(String message) {
        if (System.currentTimeMillis() - lastCall >= interval) {
            System.out.println(message);
            lastCall = System.currentTimeMillis();
        }
    }
}
