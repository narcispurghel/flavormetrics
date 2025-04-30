package com.flavormetrics.api.exception;

import com.flavormetrics.api.model.response.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiExceptions(ApiException e) {

        ApiErrorResponse response = new ApiErrorResponse(
                e.getMessage(),
                e.getDescription(),
                e.getHttpStatusCode().value(),
                e.getPath());

        return new ResponseEntity<>(response, e.getHttpStatusCode());
    }
}
