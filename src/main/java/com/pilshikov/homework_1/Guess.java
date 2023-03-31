package com.pilshikov.homework_1;

import java.util.Random;
import java.util.Scanner;

public class Guess {
    public static void main(String[] args) throws Exception{
        int number = new Random().nextInt(100);
        int maxAttempts = 10;

        System.out.println("Я загадал число, у тебя " + maxAttempts + " попыток угадать.");
        int currentAttempt;

        try(Scanner scanner = new Scanner(System.in)) {
            for (currentAttempt = 1; currentAttempt <= maxAttempts; currentAttempt++) {

                int enteredNumber = scanner.nextInt();
                if (enteredNumber != number) {
                    if (currentAttempt == maxAttempts) {
                        System.out.println("Ты не угадал");
                    } else if (enteredNumber < number) {
                        System.out.println("Мое число больше! У тебя осталось " + (maxAttempts - currentAttempt) + " попыток");
                    } else {
                        System.out.println("Мое число меньше! У тебя осталось " + (maxAttempts - currentAttempt) + " попыток");
                    }
                } else {
                    System.out.println("Ты угадал с " + currentAttempt + " попытки");
                    break;
                }
            }
        }



    }
}
