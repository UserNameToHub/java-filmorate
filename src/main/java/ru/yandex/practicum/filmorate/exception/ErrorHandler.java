package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(MyAppException.class)
    protected ResponseEntity<?> handleException(MyAppException ex, WebRequest request) {
        return ResponseEntity
                .status(ex.getResponseStatus())
                .body(Map.of("errorMessage", ex.getErrorMessage(),
                        "errorCode", ex.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleException(MethodArgumentNotValidException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errorMessage", ex.getMessage()));
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<?> handleException(Throwable ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("errorMessage", ex.getMessage()));
    }
}