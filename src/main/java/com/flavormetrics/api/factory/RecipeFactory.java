package com.flavormetrics.api.factory;

import com.flavormetrics.api.entity.Ingredient;
import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.entity.Tag;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.util.ModelConverter;

import java.time.LocalDateTime;
import java.util.List;

public final class RecipeFactory {
    private RecipeFactory() {
        // Prevent instantiation
    }

    public static Recipe getRecipe(AddRecipeRequest data, Nutritionist nutritionist) {
        final List<Ingredient> ingredients = data.ingredients()
                .stream()
                .map(ModelConverter::toIngredient)
                .toList();
        final Recipe recipe = new Recipe();
        final List<Tag> tags = data.tags().stream()
                .map(tagDto -> {
                    Tag tag = ModelConverter.toTag(tagDto);
                    tag.getRecipes().add(recipe);
                    return tag;
                })
                .toList();
        recipe.setNutritionist(nutritionist);
        recipe.setInstructions(data.instructions());
        recipe.setUpdatedAt(LocalDateTime.now());
        recipe.setImageUrl(data.imageUrl());
        recipe.setCookTimeMinutes(data.cookTimeMinutes());
        recipe.setDifficulty(data.difficulty());
        recipe.setEstimatedCalories(data.estimatedCalories());
        recipe.setPrepTimeMinutes(data.prepTimeMinutes());
        recipe.setTags(tags);
        recipe.setIngredients(ingredients);
        recipe.setName(data.name());
        return recipe;
    }
}