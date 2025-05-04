package com.flavormetrics.api.model.response;

import com.flavormetrics.api.model.RecipeDto;

import java.util.List;

public record RecipesByNutritionistResponse(String username, List<RecipeDto> recipes) {
}
