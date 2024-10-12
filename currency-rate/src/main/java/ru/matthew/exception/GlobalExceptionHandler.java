package ru.matthew.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.matthew.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(ServiceUnavailableException e) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", String.valueOf(e.getRetryAfterSeconds()));

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .headers(headers)  // Добавление заголовков в ответ
                .body(new ErrorResponse("Service Unavailable", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Bad Request", e.getBindingResult().getFieldErrors().
                        get(0).getDefaultMessage()));
    }

    @ExceptionHandler(UnsupportedCurrencyException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedCurrencyException(UnsupportedCurrencyException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Bad Request", e.getMessage()));
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCurrencyNotFoundException(CurrencyNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Not Found", e.getMessage()));
    }
}
    