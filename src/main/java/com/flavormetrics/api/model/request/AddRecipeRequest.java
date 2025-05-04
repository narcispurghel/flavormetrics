package com.flavormetrics.api.model.request;

import com.flavormetrics.api.model.IngredientDto;

import java.util.List;

public record AddRecipeRequest(String name, List<IngredientDto> ingredients) {
}
