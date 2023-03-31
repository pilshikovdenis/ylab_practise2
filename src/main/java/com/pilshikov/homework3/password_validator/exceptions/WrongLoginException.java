package com.pilshikov.homework3.password_validator.exceptions;

public class WrongLoginException extends Exception{
    public WrongLoginException(String message) {
        super(message);
    }

    public WrongLoginException() {
        super();
    }
}
