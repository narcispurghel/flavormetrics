package com.flavormetrics.api.model;

import java.util.UUID;

public record RatingWithScore(UUID recipeId, int score) {}
