package com.flavormetrics.api.model;

import java.util.UUID;

public record RatingWithAuthority(UUID recipeId, int score) {
}
