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
import com.flavormetrics.api.repository.AllergyRepository;
import com.flavormetrics.api.repository.ProfileRepository;
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
                             ProfileRepository profileRepository,
                             AllergyRepository allergyRepository) {
        return new CommandLineRunner() {
            private static final String NUTRITIONIST_USERNAME = "nutritionist@flavormetrics.com";

            @Override
            public void run(String... args) throws Exception {
                initUsers();
                initRecipes();
            }

            private void initRecipes() {
                User user = userRepository.getByUsername_Value(NUTRITIONIST_USERNAME);
                List<IngredientDto> spaghettiBologneseIngredients = new ArrayList<>();
                spaghettiBologneseIngredients.add(
                        IngredientDto.builder()
                                .name("Spaghetti")
                                .build());
                spaghettiBologneseIngredients.add(
                        IngredientDto.builder()
                                .name("Tomato Sauce")
                                .build());
                spaghettiBologneseIngredients.add(
                        IngredientDto.builder()
                                .name("GLUTEN")
                                .build());
                List<TagType> tags = new ArrayList<>();
                tags.add(TagType.ITALIAN);
                tags.add(TagType.EASY);
                List<AllergyType> allergies = new ArrayList<>();
                allergies.add(AllergyType.GLUTEN);
                allergies.add(AllergyType.SESAME);
                AddRecipeRequest spaghettiBolognese = new AddRecipeRequest(
                        "Spaghetti Bolognese",
                        spaghettiBologneseIngredients,
                        null,
                        "Boil the spaghetti, cook the beef, and mix with the tomato sauce",
                        15,
                        30,
                        DifficultyType.MEDIUM,
                        600,
                        tags,
                        allergies);
                Recipe spaghettiBologneseRecipe = RecipeFactory.getRecipe(spaghettiBolognese, (Nutritionist) user);
                recipeRepository.save(spaghettiBologneseRecipe);
            }

            private void initUsers() {
                if (!userRepository.existsByUsername_Value(NUTRITIONIST_USERNAME)) {
                    RegisterRequest req = new RegisterRequest(
                            "nutritionist@flavormetrics.com",
                            "Test",
                            "Nutritionist",
                            "testnutritionist",
                            RoleType.ROLE_NUTRITIONIST);
                    User user = UserFactory.createUser(req);
                    userRepository.save(user);
                }
                if (!userRepository.existsByUsername_Value("regular@flavormetrics.com")) {
                    RegisterRequest req = new RegisterRequest(
                            "regular@flavormetrics.com",
                            "Test",
                            "Regular",
                            "testregular",
                            RoleType.ROLE_USER);
                    User user = UserFactory.createUser(req);
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
                    User user = UserFactory.createUser(req);
                    userRepository.save(user);
                }
            }
        };
    }
}