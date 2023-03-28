package com.pilshikov.homework2.sequnces;

public class SequncesImpl implements SequenceGenerator {

    @Override
    public void a(int n) {
        int number = 2;
        for (int i = 0; i < n; i++) {
            System.out.printf("%d ", number);
            number += 2;
        }
    }

    @Override
    public void b(int n) {
        int number = 1;
        for (int i = 0; i < n; i++) {
            System.out.printf("%d ", number);
            number += 2;
        }
    }

    @Override
    public void c(int n) {
        for (int i = 1; i <= n; i++) {
            System.out.printf("%d ", i * i);
        }
    }

    @Override
    public void d(int n) {
        for (int i = 1; i <= n; i++) {
            System.out.printf("%d ", i * i * i);
        }
    }

    @Override
    public void e(int n) {
        int multiplier = 1;
        for (int i = 0; i < n; i++) {
            System.out.printf("%d ", multiplier);
            multiplier *= -1;
        }
    }

    @Override
    public void f(int n) {
        int multiplier = 1;
        for (int i = 1; i <= n; i++) {
            System.out.printf("%d ", i * multiplier);
            multiplier *= -1;
        }
    }

    @Override
    public void g(int n) {
        int increase = 3;
        int number = 1;
        int multiplier = 1;
        for (int i = 0; i < n; i++) {
            System.out.printf("%d ", number * multiplier);
            number += increase;
            increase += 2;
            multiplier *= -1;
        }
    }

    @Override
    public void h(int n) {
        boolean zero = false;
        int number = 1;
        for (int i = 0; i < n; i++) {
            if (zero) {
                System.out.printf("%d ", 0);
            } else {
                System.out.printf("%d ", number);
                number++;
            }
            zero = !zero;

        }
    }

    @Override
    public void i(int n) {
        int number = 1;
        int multiplier = 2;
        for (int i = 0; i < n; i++) {
            System.out.printf("%d ", number);
            number *= multiplier;
            multiplier++;
        }
    }

    @Override
    public void j(int n) {
        int increase = 0;
        int number = 1;
        int temp;
        for (int i = 0; i < n; i++) {
            System.out.printf("%d ", number);
            temp = number;
            number += increase;
            increase = temp;
        }
    }
}
