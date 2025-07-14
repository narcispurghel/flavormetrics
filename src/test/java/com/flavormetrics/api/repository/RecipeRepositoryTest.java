package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.*;
import com.flavormetrics.api.enums.DietaryPreferenceType;
import com.flavormetrics.api.enums.DifficultyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AllergyRepository allergyRepository;

    private User user;
    private Recipe recipe;
    private Allergy allergy;

    @BeforeEach
    void setUp() {
        Email email = new Email();
        email.setAddress("mock-email");
        user = new User();
        user.setFirstName("mock-first-name");
        user.setLastName("mock-last-name");
        user.setPasswordHash("mock-password-hash");
        user.setEmail(email);
        user = userRepository.save(user);

        Ingredient ing = new Ingredient();
        ing.setName("mock-ing-name");
        ing = ingredientRepository.save(ing);

        Tag tag = new Tag();
        tag.setName("Vegan");
        tag = tagRepository.save(tag);

        allergy = new Allergy();
        allergy.setName("mock-allergy-name");
        allergy.setDescription("mock-allergy-description");
        allergy = allergyRepository.save(allergy);

        recipe = new Recipe();
        recipe.setName("Salad");
        recipe.setPrepTimeMinutes(5);
        recipe.setCookTimeMinutes(0);
        recipe.setEstimatedCalories(200);
        recipe.setDifficulty(DifficultyType.easy);
        recipe.setDietaryPreferences(DietaryPreferenceType.vegan);
        recipe.setUser(user);
        recipe.setIngredients(Set.of(ing));
        recipe.setTags(Set.of(tag));
        recipe.setAllergies(Set.of(allergy));
        recipe = recipeRepository.save(recipe);
    }

    @Test
    void testIf_getRecipeByIdEager_ReturnsNotEmpty() {
        var result = recipeRepository.getRecipeByIdEager(recipe.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getIngredients()).hasSize(1);
        assertThat(result.get().getTags()).hasSize(1);
        assertThat(result.get().getAllergies()).hasSize(1);
        assertEquals(user,  result.get().getUser());
    }

    @Test
    void testIf_getRecipeByIdEager_ReturnsEmpty() {
        var result = recipeRepository.getRecipeByIdEager(UUID.randomUUID());
        assertThat(result).isEmpty();
    }

    @Test
    void testIf_FindAllByFilter_ReturnsNotEmpty() {
        var result = recipeRepository.getRecipeByIdEager(recipe.getId());
        Page<Recipe> page = recipeRepository.findAllByFilter(
                10, 300, 10,
                DifficultyType.easy,
                DietaryPreferenceType.vegan,
                PageRequest.of(0, 10)
        );
        assertThat(page.getContent()).isNotEmpty();
        assertEquals(1, page.getTotalElements());
        assertEquals(recipe, page.getContent().getFirst());
    }

    @Test
    void testIf_FindAllByFilter_ReturnsEmpty() {
        Page<Recipe> page = recipeRepository.findAllByFilter(
                100, 199, 5,
                DifficultyType.easy,
                DietaryPreferenceType.vegan,
                PageRequest.of(0, 10)
        );
        assertThat(page.getContent()).isEmpty();
    }

    @Test
    void testIf_FindByOwner_ReturnsNotEmpty() {
        Page<Recipe> page = recipeRepository.findByOwner("mock-email", PageRequest.of(0, 5));
        assertThat(page.getContent()).isNotEmpty();
        assertEquals(recipe, page.getContent().getFirst());
    }

    @Test
    void testIf_FindByOwner_ReturnsEmpty() {
        Page<Recipe> page = recipeRepository.findByOwner("mock-email-with-no-recipes", PageRequest.of(0, 5));
        assertThat(page.getContent()).isEmpty();
    }

    @Test
    void testIf_findAllRecommendations_ReturnsNotEmpty() {
        var allergy = new Allergy();
        allergy.setName("mock-allergy-for-profile");
        allergy.setDescription("mock-allergy-description-for-profile");
        allergy = allergyRepository.save(allergy);
        var profile = new Profile();
        profile.setAllergies(Set.of(allergy));
        profile.setUser(user);
        profileRepository.save(profile);
        Page<Recipe> page = recipeRepository.findAllRecommendations(user.getId(), PageRequest.of(0, 5));
        assertThat(page.getContent()).isNotEmpty();
        assertEquals(recipe, page.getContent().getFirst());
    }

    @Test
    void testIf_findAllRecommendations_ReturnsEmpty() {
        var allergySaved = allergyRepository.getReferenceById(allergy.getId());
        var profile = new Profile();
        profile.setAllergies(Set.of(allergySaved));
        profile.setUser(user);
        profileRepository.save(profile);
        Page<Recipe> page = recipeRepository.findAllRecommendations(user.getId(), PageRequest.of(0, 5));
        assertThat(page.getContent()).isEmpty();
    }
}
