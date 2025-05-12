package com.flavormetrics.api.config;

import com.flavormetrics.api.entity.Allergy;
import com.flavormetrics.api.entity.Profile;
import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.entity.user.impl.RegularUser;
import com.flavormetrics.api.factory.RecipeFactory;
import com.flavormetrics.api.factory.UserFactory;
import com.flavormetrics.api.model.IngredientDto;
import com.flavormetrics.api.model.enums.*;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.model.request.RegisterRequest;
import com.flavormetrics.api.repository.RecipeRepository;
import com.flavormetrics.api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DatabaseInitConfig {

    @Bean
    CommandLineRunner initDb(UserRepository userRepository,
                             RecipeRepository recipeRepository,
                             UserFactory userFactory) {
        return new CommandLineRunner() {
            private static final String NUTRITIONIST_USERNAME = "nutritionist@flavormetrics.com";

            @Override
            public void run(String... args) throws Exception {
                initUsers();
                initRecipes();
            }

            private void initRecipes() {
                final User user = userRepository.getByUsername_Value(NUTRITIONIST_USERNAME);
                final List<Recipe> recipes = new ArrayList<>();
                final List<AddRecipeRequest> recipeRequests = List.of(
                    getSpaghettiBolognese(),
                    getCapreseSalad(),
                    getChickenCurry(),
                    getGreekSalad(),
                    getVeganBurrito()
                );
                for (AddRecipeRequest req : recipeRequests) {
                    recipes.add(RecipeFactory.getRecipe(req, (Nutritionist) user));
                }
                recipeRepository.saveAll(recipes);
            }

            private void initUsers() {
                if (!userRepository.existsByUsername_Value(NUTRITIONIST_USERNAME)) {
                    RegisterRequest req = new RegisterRequest(
                            NUTRITIONIST_USERNAME,
                            "Test",
                            "Nutritionist",
                            "testnutritionist",
                            RoleType.ROLE_NUTRITIONIST);
                    User user = userFactory.createUser(req);
                    userRepository.save(user);
                }
                if (!userRepository.existsByUsername_Value("regular@flavormetrics.com")) {
                    RegisterRequest req = new RegisterRequest(
                            "regular@flavormetrics.com",
                            "Test",
                            "Regular",
                            "testregular",
                            RoleType.ROLE_USER);
                    User user = userFactory.createUser(req);
                    Profile profile = new Profile();
                    List<Allergy> allergies = new ArrayList<>();
                    Allergy allergy = new Allergy(AllergyType.GLUTEN);
                    allergy.setProfile(profile);
                    allergies.add(allergy);
                    profile.setAllergies(allergies);
                    profile.setUser((RegularUser) user);
                    profile.setDietaryPreference(DietaryPreferenceType.VEGETARIAN);
                    ((RegularUser) user).setProfile(profile);
                    userRepository.save(user);
                }
                if (!userRepository.existsByUsername_Value("admin@flavormetrics.com")) {
                    RegisterRequest req = new RegisterRequest(
                            "admin@flavormetrics.com",
                            "Test",
                            "Admin",
                            "testadmin",
                            RoleType.ROLE_ADMIN);
                    User user = userFactory.createUser(req);
                    userRepository.save(user);
                }
            }

            private AddRecipeRequest getSpaghettiBolognese() {
                final List<IngredientDto> spaghettiBologneseIngredients = List.of(
                        IngredientDto.builder()
                                .name("Spaghetti")
                                .build(),
                        IngredientDto.builder()
                                .name("GLUTEN")
                                .build()
                );
                final List<TagType> tags = List.of(TagType.ITALIAN, TagType.EASY);
                final List<AllergyType> allergies = new ArrayList<>();
                allergies.add(AllergyType.WHEAT);
                final List<DietaryPreferenceType> dietaryPreferences = new ArrayList<>();
                dietaryPreferences.add(DietaryPreferenceType.DIABETIC_FRIENDLY);
                dietaryPreferences.add(DietaryPreferenceType.LOW_FAT);
                return new AddRecipeRequest(
                        "Spaghetti Bolognese",
                        spaghettiBologneseIngredients,
                        null,
                        "Boil the spaghetti, cook the beef, and mix with the tomato sauce",
                        15,
                        30,
                        DifficultyType.MEDIUM,
                        600,
                        tags,
                        allergies,
                        dietaryPreferences);
            }

            private AddRecipeRequest getGreekSalad() {
                final List<IngredientDto> ingredients = List.of(
                        IngredientDto.builder()
                                .name("Tortilla")
                                .build(),
                        IngredientDto.builder()
                                .name("Black Beans")
                                .build(),
                        IngredientDto.builder()
                                .name("Rice")
                                .build(),
                        IngredientDto.builder()
                                .name("Avocado")
                                .build()
                );
                final List<TagType> tags = List.of(TagType.GREEK, TagType.EASY);
                final List<AllergyType> allergies = List.of(AllergyType.DAIRY);
                final List<DietaryPreferenceType> dietaryPreferences = List.of(DietaryPreferenceType.VEGETARIAN, DietaryPreferenceType.LOW_CARB);
                return new AddRecipeRequest(
                        "Greek Salad",
                        ingredients,
                        null,
                        "Chop vegetables, add feta and olives, and drizzle with olive oil.",
                        10,
                        0,
                        DifficultyType.EASY,
                        200,
                        tags,
                        allergies,
                        dietaryPreferences
                );
            }

            private AddRecipeRequest getChickenCurry() {
                final List<IngredientDto> ingredients = List.of(
                        IngredientDto.builder()
                                .name("Chicken Breast")
                                .build(),
                        IngredientDto.builder()
                                .name("Curry Powder")
                                .build(),
                        IngredientDto.builder()
                                .name("Coconut Milk")
                                .build()
                );
                final List<TagType> tags = List.of(
                        TagType.ASIAN,
                        TagType.SPICY);
                final List<AllergyType> allergies = List.of(
                        AllergyType.EGGS);
                final List<DietaryPreferenceType> dietaryPreferences = List.of(DietaryPreferenceType.HIGH_PROTEIN);
                return new AddRecipeRequest(
                        "Chicken Curry",
                        ingredients,
                        null,
                        "Cook chicken, add curry powder and coconut milk, simmer until done.",
                        20,
                        25,
                        DifficultyType.MEDIUM,
                        450,
                        tags,
                        allergies,
                        dietaryPreferences
                );
            }

            private AddRecipeRequest getVeganBurrito() {
                List<IngredientDto> ingredients = List.of(
                        IngredientDto.builder()
                                .name("Tortilla")
                                .build(),
                        IngredientDto.builder()
                                .name("Black Beans")
                                .build(),
                        IngredientDto.builder()
                                .name("Rice")
                                .build(),
                        IngredientDto.builder()
                                .name("Avocado")
                                .build()
                );
                List<TagType> tags = List.of(TagType.MEXICAN, TagType.HEALTHY);
                List<AllergyType> allergies = new ArrayList<>();
                List<DietaryPreferenceType> dietaryPreferences = List.of(
                        DietaryPreferenceType.VEGAN,
                        DietaryPreferenceType.HIGH_FIBER);

                return new AddRecipeRequest(
                        "Vegan Burrito",
                        ingredients,
                        null,
                        "Fill tortilla with beans, rice, and avocado. Roll tightly and serve.",
                        10,
                        10,
                        DifficultyType.EASY,
                        350,
                        tags,
                        allergies,
                        dietaryPreferences
                );
            }

            private AddRecipeRequest getCapreseSalad() {
                List<IngredientDto> ingredients = List.of(
                        IngredientDto.builder().
                                name("Tomatoes")
                                .build(),
                        IngredientDto.builder()
                                .name("Mozzarella")
                                .build(),
                        IngredientDto.builder()
                                .name("Fresh Basil")
                                .build()
                );
                List<TagType> tags = List.of(TagType.ITALIAN, TagType.EASY);
                List<AllergyType> allergies = List.of(AllergyType.DAIRY);
                List<DietaryPreferenceType> dietaryPreferences = List.of(
                        DietaryPreferenceType.VEGETARIAN, DietaryPreferenceType.LOW_CARB);
                return new AddRecipeRequest(
                        "Caprese Salad",
                        ingredients,
                        null,
                        "Slice tomatoes and mozzarella, layer with fresh basil, and drizzle with olive oil.",
                        5,
                        0,
                        DifficultyType.EASY,
                        220,
                        tags,
                        allergies,
                        dietaryPreferences
                );
            }

        };
    }
}