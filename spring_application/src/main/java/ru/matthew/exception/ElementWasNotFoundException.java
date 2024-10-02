package ru.matthew.exception;

public class ElementWasNotFoundException extends RuntimeException {
    public ElementWasNotFoundException(String message) {
        super(message);
    }
}
