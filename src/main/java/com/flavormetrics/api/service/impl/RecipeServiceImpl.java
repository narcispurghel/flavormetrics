package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.Ingredient;
import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.exception.impl.InvalidArgumentException;
import com.flavormetrics.api.exception.impl.NotAllowedRequestException;
import com.flavormetrics.api.exception.impl.RecipeNotFoundException;
import com.flavormetrics.api.factory.RecipeFactory;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.RecipeDto;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.model.response.AddRecipeResponse;
import com.flavormetrics.api.model.response.RecipesByNutritionistResponse;
import com.flavormetrics.api.repository.IngredientRepository;
import com.flavormetrics.api.repository.NutritionistRepository;
import com.flavormetrics.api.repository.RecipeRepository;
import com.flavormetrics.api.repository.UserRepository;
import com.flavormetrics.api.service.RecipeService;
import com.flavormetrics.api.util.ModelConverter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final NutritionistRepository nutritionistRepository;
    private final UserRepository userRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             IngredientRepository ingredientRepository,
                             NutritionistRepository nutritionistRepository,
                             UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.nutritionistRepository = nutritionistRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Data<AddRecipeResponse> add(AddRecipeRequest data, Authentication authentication) {
        if (data == null) {
            return null;
        }
        if (data.ingredients() == null || data.ingredients().isEmpty()) {
            throw new InvalidArgumentException(
                    "Invalid ingredients",
                    "Missing recipe ingredients",
                    HttpStatus.BAD_REQUEST,
                    "data.ingredients");
        }
        data.ingredients().forEach(i -> {
            if (!ingredientRepository.existsByName(i.name())) {
                ingredientRepository.save(ModelConverter.toIngredient(i));
            }
        });
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InsufficientAuthenticationException(
                    "This resource requests full authentication");
        }
        final Nutritionist nutritionist = nutritionistRepository.getByUsername_Value(
                        authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Recipe recipe = RecipeFactory.getRecipe(data, nutritionist);
        recipe = recipeRepository.save(recipe);
        return Data.body(ModelConverter.toAddRecipeResponse(recipe));
    }

    @Override
    public Data<AddRecipeResponse> getById(UUID id) {
        Recipe recipe = recipeRepository.getRecipeById(id)
                .orElseThrow(() -> new RecipeNotFoundException(
                        "Invalid id", "Cannot find recipe associated with id " + id, HttpStatus.NOT_FOUND, "recipe"));
        return Data.body(ModelConverter.toAddRecipeResponse(recipe));
    }

    @Override
    public Data<RecipesByNutritionistResponse> getByNutritionist(String username) {
        if (userRepository.existsByUsername_Value(username)) {
            List<Recipe> recipes = recipeRepository.getRecipesByNutritionist_Username_Value(username);
            return Data.body(ModelConverter.toRecipesByNutritionistResponse(username, recipes));
        }
        throw new UsernameNotFoundException("Cannot find a nutritionist account associated with username " + username);
    }

    @Override
    public Data<AddRecipeResponse> updateById(UUID id, AddRecipeRequest data, Authentication authentication) {
        if (id == null || data == null) {
            throw new InvalidArgumentException(
                    "Invalid id or data", "Missing id or data", HttpStatus.BAD_REQUEST, "data.recipe");
        }
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(
                        "Invalid id", "Cannot find a recipe associated with id " + id, HttpStatus.NOT_FOUND, "recipe.id"));
        if (!recipe.getNutritionist().getUsername().equals(authentication.getName())) {
            throw new NotAllowedRequestException(
                    "Bad request",
                    "Cannot modify a recipe that you don't own",
                    HttpStatus.BAD_REQUEST,
                    "recipe.nutritionist");
        }
        final List<Ingredient> ingredients = data.ingredients()
                .stream()
                .map(ModelConverter::toIngredient)
                .toList();
        recipe.setIngredients(ingredients);
        recipe.setName(data.name());
        recipe = recipeRepository.save(recipe);
        return Data.body(ModelConverter.toAddRecipeResponse(recipe));
    }

    @Override
    public Data<String> deleteById(UUID id) {
        if (id == null) {
            throw new InvalidArgumentException(
                    "Invalid id", "Missing id", HttpStatus.BAD_REQUEST, "data.recipe");
        }
        recipeRepository.deleteById(id);
        return Data.body("Successfully deleted recipe " + id);
    }

    @Override
    public Data<List<RecipeDto>> getAll() {
        List<RecipeDto> recipes = recipeRepository.findAll()
                .stream()
                .map(ModelConverter::toRecipeDto)
                .toList();
        return Data.body(recipes);
    }
}