package com.flavormetrics.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flavormetrics.api.model.enums.DietaryPreferenceType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ProfileDto(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UUID id,
        DietaryPreferenceType dietaryPreference,
        List<AllergyDto> allergies,
        @JsonIgnore
        UUID userId
) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private DietaryPreferenceType dietaryPreference = DietaryPreferenceType.NONE; // Default value
        private List<AllergyDto> allergies = new ArrayList<>();
        private UUID userId;

        private Builder() {
            // Prevent instantiation
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder dietaryPreference(DietaryPreferenceType dietaryPreference) {
            this.dietaryPreference = dietaryPreference;
            return this;
        }

        public Builder allergies(List<AllergyDto> allergies) {
            this.allergies = new ArrayList<>(allergies);
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public ProfileDto build() {
            return new ProfileDto(id, dietaryPreference, allergies, userId);
        }
    }
}
