package com.flavormetrics.api.model.request;

import com.flavormetrics.api.model.AllergyDto;
import com.flavormetrics.api.model.IngredientDto;
import com.flavormetrics.api.model.TagDto;
import com.flavormetrics.api.model.enums.AllergyType;
import com.flavormetrics.api.model.enums.DietaryPreferenceType;
import com.flavormetrics.api.model.enums.DifficultyType;
import com.flavormetrics.api.model.enums.TagType;

import java.util.Set;
import java.util.UUID;

public record AddRecipeRequest(
        String name,
        Set<IngredientDto> ingredients,
        String imageUrl,
        String instructions,
        Integer prepTimeMinutes,
        Integer cookTimeMinutes,
        DifficultyType difficulty,
        Integer estimatedCalories,
        Set<TagDto> tags,
        Set<AllergyDto> allergies,
        DietaryPreferenceType dietaryPreferences
) {}
