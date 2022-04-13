package com.example.mywebsite.exception;

public class UserNotEmailAuthException extends RuntimeException {
    public UserNotEmailAuthException(String msg) {
        super(msg);
    }
}
