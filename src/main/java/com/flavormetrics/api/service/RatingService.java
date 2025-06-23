package com.flavormetrics.api.service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.flavormetrics.api.model.RatingDto;

public interface RatingService {

    Map<String, String> addRecipeRating(UUID recipeId, int rating);

    Set<RatingDto> findAllRatingsByRecipeId(UUID recipeId);

    Set<RatingDto> findAllRatingsByUserId(UUID userId);

}
