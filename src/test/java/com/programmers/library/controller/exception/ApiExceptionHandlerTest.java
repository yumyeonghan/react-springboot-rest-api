package com.programmers.library.controller.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {

    private ApiExceptionHandler apiExceptionHandler = new ApiExceptionHandler();

    @DisplayName("IllegalArgumentException 발생하면 예외를 응답한다.")
    @Test
    void handleIllegalArgumentException() {
        //when
        String errorMessage = "Invalid argument";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        //then
        ErrorResult errorResult = apiExceptionHandler.handleIllegalArgumentException(exception);

        //then
        assertThat(errorResult.httpStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResult.errorMessage()).isEqualTo(errorMessage);
    }

    @DisplayName("Exception 발생하면 예외를 응답한다.")
    @Test
    void handleException() {
        //when
        String errorMessage = "Exception error";
        Exception exception = new Exception(errorMessage);

        //then
        ErrorResult errorResult = apiExceptionHandler.handleException(exception);

        //then
        assertThat(errorResult.httpStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(errorResult.errorMessage()).isEqualTo(errorMessage);
    }

    @DisplayName("RuntimeException 발생하면 예외를 응답한다.")
    @Test
    void handleRuntimeException() {
        //when
        String errorMessage = "RuntimeException error";
        RuntimeException exception = new RuntimeException(errorMessage);

        //then
        ErrorResult errorResult = apiExceptionHandler.handleRuntimeException(exception);

        //then
        assertThat(errorResult.httpStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResult.errorMessage()).isEqualTo(errorMessage);
    }
}
