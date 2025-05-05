package com.flavormetrics.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flavormetrics.api.entity.Recipe;
import java.util.List;
import java.util.UUID;

public record IngredientDto(
        @JsonIgnore
        UUID id,
        String name,
        @JsonIgnore
        List<Recipe> recipes
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private List<Recipe> recipes;

        private Builder() {
            // Prevent instantiation
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder recipes(List<Recipe> recipes) {
            this.recipes = recipes;
            return this;
        }

        public IngredientDto build() {
            return new IngredientDto(id, name, recipes);
        }
    }
}
