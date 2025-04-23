package com.flavormetrics.api.exception.impl;

import org.springframework.http.HttpStatusCode;

import com.flavormetrics.api.exception.ApiException;

public class NotAllowedRequestException extends ApiException {

    public NotAllowedRequestException(String message, String description, HttpStatusCode httpStatusCode,
            String path) {
        super(message, description, httpStatusCode, path);
    }

}