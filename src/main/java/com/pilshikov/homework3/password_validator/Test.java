package com.pilshikov.homework3.password_validator;

public class Test {
    public static void main(String[] args) {

        String login = "viktor12";
        String password = "genesis122";
        String confirmPassword = "genesis122";
        System.out.println(PasswordValidator.validate(login, password, confirmPassword));
    }
}
