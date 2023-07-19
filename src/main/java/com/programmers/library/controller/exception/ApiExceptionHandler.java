package com.programmers.library.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.programmers.library.controller.api")
public class ApiExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleIllegalArgumentException(IllegalArgumentException e) {
        return new ErrorResult(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult handleException(Exception e) {
        return new ErrorResult(HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResult handleRuntimeException(RuntimeException e) {
        return new ErrorResult(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()), e.getMessage());
    }
}
