package ru.matthew.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.matthew.dto.ErrorJsonDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ElementWasNotFoundException.class)
    public ResponseEntity<ErrorJsonDTO> handleElementWasNotFoundException(ElementWasNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorJsonDTO("Not found", e.getMessage()));
    }

    @ExceptionHandler(ElementAlreadyExistsException.class)
    public ResponseEntity<ErrorJsonDTO> handleElementAlreadyExistsException(ElementAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorJsonDTO("Conflict", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorJsonDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorJsonDTO("Bad Request", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorJsonDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorJsonDTO("Bad Request", e.getBindingResult().getFieldErrors().
                        get(0).getDefaultMessage()));
    }

    @ExceptionHandler(UnsupportedCurrencyException.class)
    public ResponseEntity<ErrorJsonDTO> handleUnsupportedCurrencyException(UnsupportedCurrencyException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorJsonDTO("Bad Request", e.getMessage()));
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ErrorJsonDTO> handleCurrencyNotFoundException(CurrencyNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorJsonDTO("Not Found", e.getMessage()));
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorJsonDTO> handleServiceUnavailableException(ServiceUnavailableException e) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", String.valueOf(e.getRetryAfterSeconds()));

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .headers(headers)
                .body(new ErrorJsonDTO("Service Unavailable", e.getMessage()));
    }
}
