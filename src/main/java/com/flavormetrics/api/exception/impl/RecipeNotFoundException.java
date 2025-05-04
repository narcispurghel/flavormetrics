package com.flavormetrics.api.exception.impl;

import com.flavormetrics.api.exception.ApiException;
import org.springframework.http.HttpStatusCode;

public class RecipeNotFoundException extends ApiException {
    public RecipeNotFoundException(String message, String description, HttpStatusCode httpStatusCode, String path) {
        super(message, description, httpStatusCode, path);
    }

    public RecipeNotFoundException(String description, HttpStatusCode httpStatusCode, String path, Throwable cause) {
        super(description, httpStatusCode, path, cause);
    }

    public RecipeNotFoundException(String message, String description, HttpStatusCode httpStatusCode, String path, Throwable cause) {
        super(message, description, httpStatusCode, path, cause);
    }

    public RecipeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String description, HttpStatusCode httpStatusCode, String path) {
        super(message, cause, enableSuppression, writableStackTrace, description, httpStatusCode, path);
    }
}