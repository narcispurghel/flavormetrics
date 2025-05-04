package com.flavormetrics.api.model.request;

import com.flavormetrics.api.model.enums.RoleType;

public record RegisterRequest(
        String username,
        String firstName,
        String lastName,
        String password,
        RoleType role) {

}
