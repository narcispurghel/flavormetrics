package com.flavormetrics.api.model.request;

import com.flavormetrics.api.model.IngredientDto;
import com.flavormetrics.api.model.enums.AllergyType;
import com.flavormetrics.api.model.enums.DietaryPreferenceType;
import com.flavormetrics.api.model.enums.DifficultyType;
import com.flavormetrics.api.model.enums.TagType;

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
        List<TagType> tags,
        List<AllergyType> allergies,
        List<DietaryPreferenceType> dietaryPreferences) {
}
