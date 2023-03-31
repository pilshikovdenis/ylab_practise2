package com.pilshikov.homework2.complex_numbers;

public class ComplexNumber {
    public double real;
    public double imaginary;

    public ComplexNumber(double r) {
        this.real = r;
        this.imaginary = 0;
    }

    public ComplexNumber(double r, double im ) {
        this.real = r;
        this.imaginary = im;
    }

    public static ComplexNumber add (ComplexNumber first, ComplexNumber second) {
        return new ComplexNumber(first.real + second.real,
                first.imaginary + second.imaginary);
    }

    public static ComplexNumber subtract(ComplexNumber first, ComplexNumber second) {
        return new ComplexNumber(first.real - second.real,
                first.imaginary - second.imaginary);
    }



    public static ComplexNumber multiply(ComplexNumber first, ComplexNumber second) {
        return new ComplexNumber(
                (first.real * second.real) - (first.imaginary * second.imaginary),
                (first.real * second.imaginary) + (first.imaginary * second.real)
        );
    }

    public double getModulus() {
        return Math.abs(
                Math.sqrt( Math.pow(this.real, 2) + Math.pow(this.imaginary, 2))
        );
    }

    @Override
    public String toString() {
        return "z = " + real + " + " + imaginary + "y";
    }
}
