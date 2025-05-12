package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.*;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.exception.impl.InvalidArgumentException;
import com.flavormetrics.api.exception.impl.NotAllowedRequestException;
import com.flavormetrics.api.exception.impl.ProfileNotFoundException;
import com.flavormetrics.api.exception.impl.RecipeNotFoundException;
import com.flavormetrics.api.factory.RecipeFactory;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.ProfileFilter;
import com.flavormetrics.api.model.RecipeDefaultFilter;
import com.flavormetrics.api.model.RecipeDto;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.model.response.RecipesByNutritionistResponse;
import com.flavormetrics.api.repository.*;
import com.flavormetrics.api.service.RecipeService;
import com.flavormetrics.api.util.ModelConverter;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RecipeServiceImpl implements RecipeService {
    private static final String RECIPE_NOT_FOUND_MESSAGE = "Cannot find a recipe associated with id ";

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final NutritionistRepository nutritionistRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             IngredientRepository ingredientRepository,
                             NutritionistRepository nutritionistRepository,
                             UserRepository userRepository,
                             ProfileRepository profileRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.nutritionistRepository = nutritionistRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    @Transactional
    public Data<RecipeDto> add(AddRecipeRequest data, Authentication authentication) {
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
        return Data.body(ModelConverter.toRecipeDto(recipe));
    }

    @Override
    @Transactional
    public Data<RecipeDto> getById(UUID id) {
        Recipe recipe = recipeRepository.getRecipeById(id)
                .orElseThrow(() -> new RecipeNotFoundException(
                        "Invalid id", "Cannot find recipe associated with id " + id, HttpStatus.NOT_FOUND, "recipe"));
        return Data.body(ModelConverter.toRecipeDto(recipe));
    }

    @Override
    @Transactional
    public Data<RecipesByNutritionistResponse> getByNutritionist(String username) {
        if (userRepository.existsByUsername_Value(username)) {
            List<Recipe> recipes = recipeRepository.getRecipesByNutritionist_Username_Value(username);
            return Data.body(ModelConverter.toRecipesByNutritionistResponse(username, recipes));
        }
        throw new UsernameNotFoundException("Cannot find a nutritionist account associated with username " + username);
    }

    @Override
    @Transactional
    public Data<RecipeDto> updateById(UUID id, AddRecipeRequest data, Authentication authentication) {
        if (id == null || data == null) {
            throw new InvalidArgumentException(
                    "Invalid id or data", "Missing id or data", HttpStatus.BAD_REQUEST, "data.recipe");
        }
        final Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(
                        "Invalid id", RECIPE_NOT_FOUND_MESSAGE + id, HttpStatus.NOT_FOUND, "recipe.id"));
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
        final List<Tag> tags = data.tags().stream()
                .map(tagDto -> {
                    Tag tag = ModelConverter.toTag(tagDto);
                    tag.getRecipes().add(recipe);
                    return tag;
                })
                .toList();
        final List<Allergy> allergies = data.allergies()
                .stream()
                .map(ModelConverter::toAllergy)
                .toList();
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
        Recipe saved = recipeRepository.save(recipe);
        return Data.body(ModelConverter.toRecipeDto(saved));
    }

    @Override
    @Transactional
    public Data<String> deleteById(UUID id, Authentication authentication) {
        if (id == null) {
            throw new InvalidArgumentException(
                    "Invalid id", "Missing id", HttpStatus.BAD_REQUEST, "data.recipe");
        }
        final Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(
                        "Cannot perform the delete operation",
                        RECIPE_NOT_FOUND_MESSAGE + id,
                        HttpStatus.NOT_FOUND,
                        "id"));
        final String owner = recipe.getNutritionist().getUsername();
        if (!authentication.getName().equals(owner)) {
            throw new NotAllowedRequestException(
                    "Cannot perform the delete operation",
                    "You are not the owner of the recipe",
                    HttpStatus.FORBIDDEN,
                    "authorization");
        }
        recipeRepository.deleteById(id);
        return Data.body("Successfully deleted recipe " + id);
    }

    @Override
    public Data<List<RecipeDto>> getAll() {
        final List<RecipeDto> recipes = recipeRepository.findAll()
                .stream()
                .map(ModelConverter::toRecipeDto)
                .toList();
        return Data.body(recipes);
    }

    @Override
    @Transactional
    public ProfileFilter getProfilePreferences(String username) {
        final Profile profile = profileRepository.findByUser_Username_Value(username)
                .orElseThrow(() -> new ProfileNotFoundException(
                        "Not found", "Invalid username or profile is not created", HttpStatus.NOT_FOUND, "profile"));
        return new ProfileFilter(profile.getDietaryPreference(), profile.getAllergies());
    }

    @Override
    public Data<List<RecipeDto>> findAllByProfilePreferences(ProfileFilter profileFilter) {
        final List<Recipe> recipes = recipeRepository.getAllByProfileFilters(
                profileFilter.allergiesToString(), profileFilter.dietaryPreference().name());
        final List<RecipeDto> recipesDto = recipes.stream()
                .map(ModelConverter::toRecipeDto)
                .toList();
        return Data.body(recipesDto);
    }

    @Override
    public Data<List<RecipeDto>> findAllByDefaultFilter(RecipeDefaultFilter recipeDefaultFilter) {
        final List<Recipe> recipes = recipeRepository.findAllByDefaultFilter(
                recipeDefaultFilter.prepTimeMinutes(),
                recipeDefaultFilter.cookTimeMinutes(),
                recipeDefaultFilter.estimatedCalories(),
                recipeDefaultFilter.difficulty());
        final List<RecipeDto> recipesDto = recipes.stream()
                .map(ModelConverter::toRecipeDto)
                .toList();
        return Data.body(recipesDto);
    }

    @Override
    public boolean existsById(UUID id) {
        return recipeRepository.existsById(id);
    }

    @Override
    public Data<RecipeDto> updateImageById(UUID id, String url) {
        final Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(
                        "Invalid recipe id",
                        RECIPE_NOT_FOUND_MESSAGE + id,
                        HttpStatus.BAD_REQUEST,
                        "id"));
        recipe.setImageUrl(url);
        return Data.body(ModelConverter.toRecipeDto(recipe));
    }
}