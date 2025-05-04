package com.flavormetrics.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flavormetrics.api.entity.user.impl.Nutritionist;

import java.util.List;
import java.util.UUID;

public record RecipeDto(
        @JsonIgnore
        UUID id,
        String name,
        @JsonIgnore
        Nutritionist nutritionist,
        List<IngredientDto> ingredients
) {

    public static class Builder {
        private UUID id;
        private String name;
        private Nutritionist nutritionist;
        private List<IngredientDto> ingredients;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder nutritionist(Nutritionist nutritionist) {
            this.nutritionist = nutritionist;
            return this;
        }

        public Builder ingredients(List<IngredientDto> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeDto build() {
            return new RecipeDto(id, name, nutritionist, ingredients);
        }
    }
}
