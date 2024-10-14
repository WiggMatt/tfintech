package ru.matthew.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.matthew.exception.RateLimitExceededException;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

@Component
public class RateLimiter {
    private final Semaphore semaphore;

    public RateLimiter(@Value("${rate.limiter.limit}") int limit) {
        this.semaphore = new Semaphore(limit);
    }

    public <T> T executeWithLimit(Callable<T> task) throws Exception {
        if (!semaphore.tryAcquire()) {
            throw new RateLimitExceededException("Rate limit exceeded. Please try again later.");
        }
        try {
            return task.call();
        } finally {
            semaphore.release();
        }
    }
}
