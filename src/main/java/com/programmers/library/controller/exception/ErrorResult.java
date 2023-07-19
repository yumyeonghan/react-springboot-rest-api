package com.programmers.library.controller.exception;

import org.springframework.http.HttpStatusCode;

public record ErrorResult(HttpStatusCode httpStatusCode, String errorMessage) {
}
