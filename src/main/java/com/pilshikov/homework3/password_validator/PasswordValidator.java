package com.pilshikov.homework3.password_validator;

import com.pilshikov.homework3.password_validator.exceptions.WrongLoginException;
import com.pilshikov.homework3.password_validator.exceptions.WrongPasswordException;


public class PasswordValidator {

    public static boolean validate(String login, String password, String confirmPassword) {
        try {
             if (validateLogin(login) && validatePassword(password) && validateConfirmPassword(password, confirmPassword))
                 return true;
        } catch (WrongLoginException | WrongPasswordException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private static boolean validateLogin(String login) throws WrongLoginException {
        String loginPattern = "^[a-zA-Z0-9_]+$";
        if (login.isEmpty()) {
            throw new WrongLoginException("Логин не может быть пустым");
        }
        if (! login.matches(loginPattern)) {
            throw new WrongLoginException("Логин содержит недопустимые символы");
        }
        if (login.length() >= 20) {
            throw new WrongLoginException("Логин слишком длинный");
        }

        return true;
    }

    private static boolean validatePassword(String password) throws WrongPasswordException {
        String passwordPattern = "^[a-zA-Z0-9_]+$";
        if (password.isEmpty()) {
            throw new WrongPasswordException("Пароль не может быть пустым");
        }
        if (! password.matches(passwordPattern)) {
            throw new WrongPasswordException("Пароль содержит недопустимые символы");
        }
        if (password.length() >= 20) {
            throw new WrongPasswordException("Пароль слишком длинный");
        }

        return true;
    }

    private static boolean validateConfirmPassword(String password, String confirm) throws WrongPasswordException {
        if (! password.equals(confirm)) {
            throw new WrongPasswordException("Пароль и подтверждение не совпадают");
        }
        return true;
    }


}
