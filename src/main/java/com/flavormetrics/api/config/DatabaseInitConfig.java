package com.flavormetrics.api.config;

import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.factory.RecipeFactory;
import com.flavormetrics.api.factory.UserFactory;
import com.flavormetrics.api.model.IngredientDto;
import com.flavormetrics.api.model.TagDto;
import com.flavormetrics.api.model.enums.DifficultyType;
import com.flavormetrics.api.model.enums.RoleType;
import com.flavormetrics.api.model.enums.TagType;
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
    CommandLineRunner initDb(UserRepository userRepository, RecipeRepository recipeRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                initUsers();
                initRecipes();
            }

            private void initRecipes() {
                User user = userRepository.getByUsername_Value("nutritionist@flavormetrics.com");
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
                                .name("Ground Beef")
                                .build());
                List<TagDto> tags = new ArrayList<>();
                tags.add(new TagDto(TagType.ITALIAN));
                tags.add(new TagDto(TagType.MEDIUM));
                AddRecipeRequest spaghettiBolognese = new AddRecipeRequest(
                        "Spaghetti Bolognese",
                        spaghettiBologneseIngredients,
                        null,
                        "Boil the spaghetti, cook the beef, and mix with the tomato sauce",
                        15,
                        30,
                        DifficultyType.MEDIUM,
                        600,
                        tags);

                Recipe spaghettiBologneseRecipe = RecipeFactory.getRecipe(spaghettiBolognese, (Nutritionist) user);
                recipeRepository.save(spaghettiBologneseRecipe);
            }

            private void initUsers() {
                if (!userRepository.existsByUsername_Value("nutritionist@flavormetrics.com")) {
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