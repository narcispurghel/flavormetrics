package com.flavormetrics.api.exception;

import com.flavormetrics.api.model.response.ApiErrorResponse;
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

/*    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleApiExceptions(Exception e) {

        ApiErrorResponse response = new ApiErrorResponse(
                "Unexpected error occurred",
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

}
