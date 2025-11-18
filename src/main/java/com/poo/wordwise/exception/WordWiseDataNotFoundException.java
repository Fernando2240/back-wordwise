package com.poo.wordwise.exception;

public class WordWiseDataNotFoundException extends RuntimeException {

    public WordWiseDataNotFoundException(String message) {
        super(message);
    }

    public WordWiseDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
