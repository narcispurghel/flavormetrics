package com.flavormetrics.api.model.request;

public record RegisterRequest(
        String email,
        String firstName,
        String lastName) {

}
