package com.flavormetrics.api.model;

import java.util.List;

public record RecipeByOwner(String owner, List<RecipeDto> recipes) {
}
