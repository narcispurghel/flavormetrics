package com.flavormetrics.api.exception;

import org.springframework.http.HttpStatus;

public class MaximumNumberOfRatingException extends ApiException {
    public MaximumNumberOfRatingException(String message, String description, HttpStatus httpStatus, String path) {
        super(message, description, httpStatus, path);
    }
}