package com.flavormetrics.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flavormetrics.api.model.enums.AllergyType;

import java.util.UUID;

public record AllergyDto(
        @JsonIgnore
        UUID id,
        String name,
        String description
) {

    public static class Builder {
        private UUID id;
        private String name;
        private String description;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder type(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public AllergyDto build() {
            return new AllergyDto(id, name, description);
        }
    }
}
