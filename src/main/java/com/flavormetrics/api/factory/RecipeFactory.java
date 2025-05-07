package com.flavormetrics.api.factory;

import com.flavormetrics.api.entity.Allergy;
import com.flavormetrics.api.entity.Ingredient;
import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.entity.Tag;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.repository.AllergyRepository;
import com.flavormetrics.api.util.ModelConverter;

import java.time.LocalDateTime;
import java.util.List;

public final class RecipeFactory {
    private final AllergyRepository allergyRepository;

    public RecipeFactory(AllergyRepository allergyRepository) {
        this.allergyRepository = allergyRepository;
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
        final List<Allergy> allergies = data.allergies()
                .stream()
                .map(allergyDto -> {
                    Allergy allergy = ModelConverter.toAllergy(allergyDto);
                    allergy.setRecipe(recipe);
                    return allergy;
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
        recipe.setAllergies(allergies);
        return recipe;
    }
}