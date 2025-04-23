package com.flavormetrics.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.flavormetrics.api.model.response.ApiErrorResponse;

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
