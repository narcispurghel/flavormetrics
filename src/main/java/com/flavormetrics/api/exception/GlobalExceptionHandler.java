package com.flavormetrics.api.exception;

import com.flavormetrics.api.model.response.ApiErrorResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleApiExceptions(TypeMismatchException e) {
        ApiErrorResponse response = new ApiErrorResponse(
                "Bad request",
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
