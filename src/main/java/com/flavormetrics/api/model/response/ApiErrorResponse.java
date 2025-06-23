package com.flavormetrics.api.model.response;

public record ApiErrorResponse(int code, String message) {

    public static ApiErrorResponse from(int code, String msg) {
        return new ApiErrorResponse(code, msg);
    }

}
