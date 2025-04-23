package com.flavormetrics.api.exception;

import org.springframework.http.HttpStatusCode;

public abstract class ApiException extends RuntimeException {

    private final String description;
    private final HttpStatusCode httpStatusCode;
    private final String path;

    protected ApiException(String message, String description, HttpStatusCode httpStatusCode, String path) {
        super(message);
        this.description = description;
        this.httpStatusCode = httpStatusCode;
        this.path = path;
    }

    protected ApiException(String description, HttpStatusCode httpStatusCode, String path, Throwable cause) {
        super(cause);
        this.description = description;
        this.httpStatusCode = httpStatusCode;
        this.path = path;
    }

    protected ApiException(String message, String description, HttpStatusCode httpStatusCode, String path, Throwable cause) {
        super(message, cause);
        this.description = description;
        this.httpStatusCode = httpStatusCode;
        this.path = path;
    }

    protected ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
            String description, HttpStatusCode httpStatusCode, String path) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.description = description;
        this.httpStatusCode = httpStatusCode;
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getPath() {
        return path;
    }

}