package com.flavormetrics.api.model.response;

public record ApiErrorResponse(String message, String description, int status, String path) {

}
