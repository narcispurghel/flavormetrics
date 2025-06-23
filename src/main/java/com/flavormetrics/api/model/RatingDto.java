package com.flavormetrics.api.model;

import com.flavormetrics.api.entity.Rating;

import java.util.UUID;

public record RatingDto(
        UUID recipeId,
        String user,
        int score
) {
    public RatingDto(Rating rating) {
        this(rating.getId(), rating.getUser().getEmail().getAddress(), rating.getScore());
    }
}
