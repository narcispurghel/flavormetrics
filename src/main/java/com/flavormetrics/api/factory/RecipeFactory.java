package com.flavormetrics.api.factory;

import com.flavormetrics.api.entity.Ingredient;
import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.util.ModelConverter;

import java.util.List;

public final class RecipeFactory {
    private RecipeFactory() {
        // Prevent instantiation
    }

    public static Recipe getRecipe(AddRecipeRequest data, Nutritionist nutritionist) {
        List<Ingredient> ingredients = data.ingredients()
                .stream()
                .map(ModelConverter::toIngredient)
                .toList();
        Recipe recipe = new Recipe();
        recipe.setName(data.name());
        recipe.setNutritionist(nutritionist);
        recipe.setIngredients(ingredients);
        return recipe;
    }
}