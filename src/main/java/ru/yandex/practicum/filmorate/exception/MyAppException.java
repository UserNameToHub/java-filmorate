package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

public class MyAppException extends RuntimeException {
    private String errorCode;
    private String errorMessage;
    private HttpStatus responseStatus;

    public MyAppException(String errorCode, String errorMessage, HttpStatus responseStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.responseStatus = responseStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }
}
