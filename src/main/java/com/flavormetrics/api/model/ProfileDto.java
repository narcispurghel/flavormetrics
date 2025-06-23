package com.flavormetrics.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flavormetrics.api.model.enums.DietaryPreferenceType;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Set;

public record ProfileDto(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UUID id,
        DietaryPreferenceType dietaryPreference,
        Set<AllergyDto> allergies,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UUID userId,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime createdAt,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime updatedAt
) {
}
