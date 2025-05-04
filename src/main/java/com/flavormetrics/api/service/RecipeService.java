package com.flavormetrics.api.service;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.RecipeDto;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.model.response.AddRecipeResponse;
import com.flavormetrics.api.model.response.RecipesByNutritionistResponse;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface RecipeService {
    Data<AddRecipeResponse> add(AddRecipeRequest data, Authentication authentication);

    Data<AddRecipeResponse> getById(UUID id);

    Data<RecipesByNutritionistResponse> getByNutritionist(String username);

    Data<AddRecipeResponse> updateById(UUID id, AddRecipeRequest data, Authentication authentication);

    Data<String> deleteById(UUID id);

    Data<List<RecipeDto>> getAll();
}
