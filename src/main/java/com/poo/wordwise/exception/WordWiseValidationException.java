package com.poo.wordwise.exception;

public class WordWiseValidationException extends RuntimeException {

    public WordWiseValidationException(String message) {
        super(message);
    }

    public WordWiseValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
