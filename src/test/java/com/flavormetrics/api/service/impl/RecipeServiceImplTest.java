package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.Authority;
import com.flavormetrics.api.entity.Email;
import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.factory.RecipeFactory;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.IngredientDto;
import com.flavormetrics.api.model.RecipeDto;
import com.flavormetrics.api.model.enums.*;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.model.response.RecipesByNutritionistResponse;
import com.flavormetrics.api.repository.*;
import com.flavormetrics.api.service.RecipeService;
import com.flavormetrics.api.util.ModelConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {
    private static final UUID RECIPE_ID = UUID.randomUUID();
    private static final String NUTRITIONIST_USERNAME = "Test";
    private static final String NUTRITIONIST_PASSWORD = "testpassword";
    private static final RoleType NUTRITIONIST_ROLE = RoleType.ROLE_NUTRITIONIST;
    private static final String NAME = "Test";
    private static final List<IngredientDto> INGREDIENTS = List.of(
            IngredientDto.builder()
                    .name("Salt")
                    .build()
    );
    private static final String IMAGE_URL = "Test";
    private static final String INSTRUCTIONS = "Test";
    private static final Integer PREP_TIME_MINUTES = 30;
    private static final Integer COOK_TIME_MINUTES = 60;
    private static final DifficultyType DIFFICULTY = DifficultyType.MEDIUM;
    private static final Integer ESTIMATED_CALORIES = 600;
    private static final List<TagType> TAGS = List.of(
            TagType.ITALIAN,
            TagType.HEALTHY
    );
    private static final List<AllergyType> ALLERGIES = List.of(
            AllergyType.EGGS,
            AllergyType.WHEAT
    );
    private static final List<DietaryPreferenceType> DIETARY_PREFERENCES = List.of(
            DietaryPreferenceType.NONE
    );

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private NutritionistRepository nutritionistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeServiceImpl(
                recipeRepository,
                ingredientRepository,
                nutritionistRepository,
                userRepository,
                profileRepository
        );
    }

    @Test
    void testAddRecipe() {
        final AddRecipeRequest request = new AddRecipeRequest(
                NAME,
                INGREDIENTS,
                IMAGE_URL,
                INSTRUCTIONS,
                PREP_TIME_MINUTES,
                COOK_TIME_MINUTES,
                DIFFICULTY,
                ESTIMATED_CALORIES,
                TAGS,
                ALLERGIES,
                DIETARY_PREFERENCES
        );
        final Email email = new Email(NUTRITIONIST_USERNAME);
        final Nutritionist nutritionist = new Nutritionist(NUTRITIONIST_PASSWORD, email);
        nutritionist.setAuthorities(List.of(new Authority(NUTRITIONIST_ROLE)));
        final Recipe recipe = RecipeFactory.getRecipe(request, nutritionist);
        recipe.setId(RECIPE_ID);
        Authentication authentication = new UsernamePasswordAuthenticationToken(nutritionist, null, nutritionist.getAuthorities());
        Mockito.when(nutritionistRepository.getByUsername_Value(NUTRITIONIST_USERNAME))
                .thenReturn(Optional.of(nutritionist));
        Mockito.when(ingredientRepository.existsByName(INGREDIENTS.getFirst().name()))
                .thenReturn(false);
        Mockito.when(ingredientRepository.save(ModelConverter.toIngredient(INGREDIENTS.getFirst())))
                .thenReturn(ModelConverter.toIngredient(INGREDIENTS.getFirst()));
        Mockito.when(recipeRepository.save(any(Recipe.class)))
                .thenReturn(recipe);
        final Data<RecipeDto> response = recipeService.add(request, authentication);
        assertNotNull(response);
        assertNotNull(response.data());
        assertEquals(NUTRITIONIST_USERNAME, response.data().nutritionist());
        assertEquals(RECIPE_ID, response.data().id());
        assertEquals(NAME, response.data().name());
        assertEquals(INSTRUCTIONS, response.data().instructions());
        assertEquals(IMAGE_URL, response.data().imageUrl());
        assertEquals(PREP_TIME_MINUTES, response.data().prepTimeMinutes());
        assertEquals(COOK_TIME_MINUTES, response.data().cookTimeMinutes());
        assertEquals(DIFFICULTY, response.data().difficulty());
        assertEquals(ESTIMATED_CALORIES, response.data().estimatedCalories());
        assertNull(response.data().averageRating());
        assertFalse(LocalDateTime.now().isBefore(response.data().createdAt()));
        assertFalse(LocalDateTime.now().isBefore(response.data().updatedAt()));
    }

    @Test
    void getById() {
        final AddRecipeRequest request = new AddRecipeRequest(
                NAME,
                INGREDIENTS,
                IMAGE_URL,
                INSTRUCTIONS,
                PREP_TIME_MINUTES,
                COOK_TIME_MINUTES,
                DIFFICULTY,
                ESTIMATED_CALORIES,
                TAGS,
                ALLERGIES,
                DIETARY_PREFERENCES
        );
        final Email email = new Email(NUTRITIONIST_USERNAME);
        final Nutritionist nutritionist = new Nutritionist(NUTRITIONIST_PASSWORD, email);
        nutritionist.setAuthorities(List.of(new Authority(NUTRITIONIST_ROLE)));
        final Recipe recipe = RecipeFactory.getRecipe(request, nutritionist);
        recipe.setId(RECIPE_ID);
        Mockito.when(recipeRepository.getRecipeById(RECIPE_ID))
                .thenReturn(Optional.of(recipe));
        final Data<RecipeDto> response = recipeService.getById(RECIPE_ID);
        assertNotNull(response);
        assertNotNull(response.data());
        assertEquals(NUTRITIONIST_USERNAME, response.data().nutritionist());
        assertEquals(RECIPE_ID, response.data().id());
        assertEquals(NAME, response.data().name());
        assertEquals(INSTRUCTIONS, response.data().instructions());
        assertEquals(IMAGE_URL, response.data().imageUrl());
        assertEquals(PREP_TIME_MINUTES, response.data().prepTimeMinutes());
        assertEquals(COOK_TIME_MINUTES, response.data().cookTimeMinutes());
        assertEquals(DIFFICULTY, response.data().difficulty());
        assertEquals(ESTIMATED_CALORIES, response.data().estimatedCalories());
        assertNull(response.data().averageRating());
        assertFalse(LocalDateTime.now().isBefore(response.data().createdAt()));
        assertFalse(LocalDateTime.now().isBefore(response.data().updatedAt()));
    }

    @Test
    void getByNutritionist() {
        Mockito.when(userRepository.existsByUsername_Value(NUTRITIONIST_USERNAME))
                .thenReturn(true);
        Mockito.when(recipeRepository.getRecipesByNutritionist_Username_Value(NUTRITIONIST_USERNAME))
                .thenReturn(List.of());
        final Data<RecipesByNutritionistResponse> response = recipeService.getByNutritionist(NUTRITIONIST_USERNAME);
        assertNotNull(response);
        assertNotNull(response.data());
        assertEquals(NUTRITIONIST_USERNAME, response.data().username());
        assertNotNull(response.data().recipes());
    }

}