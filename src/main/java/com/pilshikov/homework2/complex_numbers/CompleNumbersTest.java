package com.pilshikov.homework2.complex_numbers;

public class CompleNumbersTest {
    public static void main(String[] args) {
        ComplexNumber number = new ComplexNumber(3, 2);
        ComplexNumber number2 = new ComplexNumber(5, 3);

        ComplexNumber adding = ComplexNumber.add(number, number2);
        ComplexNumber subtracting = ComplexNumber.subtract(number, number2);
        ComplexNumber product = ComplexNumber.multiply(number, number2);
        double modulus = number.getModulus();


        System.out.println("number 1 : " + number);
        System.out.println("number 2 : " + number2);
        System.out.println("adding operation : " + adding);
        System.out.println("subtracting operation : " + subtracting);
        System.out.println("product operation : " + product);
        System.out.println("get modulus of number1 : " + modulus);
        System.out.println();


        ComplexNumber number3 = new ComplexNumber(4);
        ComplexNumber number4 = new ComplexNumber(3, 7);
        System.out.println("number 3 : " + number3);
        System.out.println("number 4 : " + number4);
        System.out.println("adding operation : " + ComplexNumber.add(number3, number4));
        System.out.println("subtracting operation : " + ComplexNumber.subtract(number3, number4));
        System.out.println("product operation : " + ComplexNumber.multiply(number3, number4));
        System.out.println("get modulus of number3 : " + number3.getModulus());

    }
}
