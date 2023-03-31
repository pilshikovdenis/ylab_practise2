package com.pilshikov.homework_1;

import java.util.Scanner;

public class Pell {
    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)){
            int n = scanner.nextInt();

            if (n == 0)
                System.out.println(0);
            if (n == 1)
                System.out.println(1);
            else {
                int first = 0;
                int second = 1;
                int temp;
                for (int i = 2; i <= n; i++) {
                    temp = second;
                    second = 2 * second + first;
                    first = temp;

                }
                System.out.println(second);
            }
        }
    }
}
