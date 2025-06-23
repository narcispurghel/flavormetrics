package com.flavormetrics.api.model;

import com.flavormetrics.api.model.enums.DietaryPreferenceType;
import com.flavormetrics.api.model.enums.DifficultyType;

public record RecipeFilter(
    int prepTimeMinutes,
    int cookTimeMinutes,
    int estimatedCalories,
    DifficultyType difficulty,
    DietaryPreferenceType dietaryPreference
) {
}
