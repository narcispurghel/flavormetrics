package com.flavormetrics.api.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flavormetrics.api.entity.Ingredient;

public record IngredientDto(
        @JsonIgnore
        UUID id,
        String name
) {
    public IngredientDto(Ingredient ingredient) {
        this(ingredient.getId(), ingredient.getName());
    }
}
