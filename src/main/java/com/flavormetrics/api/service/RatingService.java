package com.flavormetrics.api.service;

import com.flavormetrics.api.model.RatingDto;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface RatingService {
  Map<String, String> addRecipeRating(UUID recipeId, int rating);

  Set<RatingDto> findAllRatingsByRecipeId(UUID recipeId);

  Set<RatingDto> findAllRatingsByUserId(UUID userId);
}
