package ru.matthew.exception;

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
    public ResponseEntity<ErrorJsonDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Ошибка валидации: ");
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMessage.append(error.getField())
                .append(": ")
                .append(error.getDefaultMessage())
                .append("; "));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorJsonDTO("Bad Request", errorMessage.toString()));
    }
}
