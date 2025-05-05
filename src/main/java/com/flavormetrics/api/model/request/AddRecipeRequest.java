package com.flavormetrics.api.model.request;

import com.flavormetrics.api.model.IngredientDto;
import com.flavormetrics.api.model.TagDto;
import com.flavormetrics.api.model.enums.DifficultyType;

import java.util.List;

public record AddRecipeRequest(
        String name,
        List<IngredientDto> ingredients,
        String imageUrl,
        String instructions,
        Integer prepTimeMinutes,
        Integer cookTimeMinutes,
        DifficultyType difficulty,
        Integer estimatedCalories,
        List<TagDto> tags) {
}
