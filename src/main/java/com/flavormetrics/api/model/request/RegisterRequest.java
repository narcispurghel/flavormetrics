package com.flavormetrics.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Register request")
public record RegisterRequest(
        @Schema(description = "Unique identifier", example = "narcis@email.com")
        String email,

        @Schema(description = "User first name", example = "Narcis")
        String firstName,

        @Schema(description = "User last name", example = "Purghel")
        String lastName,

        @Schema(description = "User credentials", example = "strongPassword")
        String password
) {}
