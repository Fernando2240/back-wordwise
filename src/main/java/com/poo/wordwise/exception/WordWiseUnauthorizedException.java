package com.poo.wordwise.exception;

public class WordWiseUnauthorizedException extends RuntimeException {

    public WordWiseUnauthorizedException(String message) {
        super(message);
    }

    public WordWiseUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
