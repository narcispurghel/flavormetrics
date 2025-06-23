package com.flavormetrics.api.mapper;

import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.model.RecipeByOwner;
import com.flavormetrics.api.model.RecipeDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecipeMapper {

    RecipeMapper() {

    }

    public RecipeByOwner toRecipeByOwner(List<Recipe> recipes, String owner) {
        if (recipes == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        List<RecipeDto> dtos = recipes.stream().map(RecipeDto::new).toList();
        return new RecipeByOwner(owner, dtos);
    }

}
