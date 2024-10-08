package ru.matthew.exception;

import lombok.Getter;

@Getter
public class ServiceUnavailableException extends RuntimeException {
    private final int retryAfterSeconds;

    public ServiceUnavailableException(String message, int retryAfterSeconds) {
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
    }
}
