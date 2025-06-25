package com.flavormetrics.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flavormetrics.api.entity.Allergy;

import java.util.UUID;

public record AllergyDto(
        @JsonIgnore
        UUID id,

        String name,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String description
) {
    public AllergyDto(Allergy allergy) {
        this(allergy.getId(), allergy.getName(), allergy.getDescription());
    }
}
