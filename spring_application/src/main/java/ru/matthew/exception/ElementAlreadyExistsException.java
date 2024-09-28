package ru.matthew.exception;

public class ElementAlreadyExistsException extends RuntimeException {
    public ElementAlreadyExistsException(String message) {
        super(message);
    }
}
