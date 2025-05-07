package com.flavormetrics.api.exception.impl;

import com.flavormetrics.api.exception.ApiException;
import org.springframework.http.HttpStatusCode;

public class DuplicateEmailException extends ApiException {

    public DuplicateEmailException(String message, String description, HttpStatusCode httpStatusCode, String path) {
        super(message, description, httpStatusCode, path);
    }

}
