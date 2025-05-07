package com.flavormetrics.api.util;

import com.flavormetrics.api.entity.*;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.entity.user.impl.RegularUser;
import com.flavormetrics.api.model.*;
import com.flavormetrics.api.model.enums.AllergyType;
import com.flavormetrics.api.model.enums.TagType;
import com.flavormetrics.api.model.request.CreateProfileRequest;
import com.flavormetrics.api.model.response.AddRecipeResponse;
import com.flavormetrics.api.model.response.RecipesByNutritionistResponse;
import com.flavormetrics.api.model.response.RegisterResponse;

import java.util.List;

public class ModelConverter {

    private ModelConverter() {

    }

    public static RegisterResponse registerResponse(User user) {
        if (user == null) {
            return null;
        }

        return new RegisterResponse.Builder()
                .userId(user.getId())
                .role(user.getAuthorities().getFirst().getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .buid();
    }

    public static Ingredient toIngredient(IngredientDto ingredientDto) {
        if (ingredientDto == null) {
            return null;
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientDto.id());
        ingredient.setName(ingredientDto.name());
        ingredient.setRecipes(ingredientDto.recipes());
        return ingredient;
    }

    public static RecipeDto toRecipeDto(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        List<com.flavormetrics.api.model.IngredientDto> ingredients = recipe.getIngredients().stream()
                .map(ModelConverter::toIngredientDto)
                .toList();
        List<TagDto> tags = recipe.getTags()
                .stream()
                .map(ModelConverter::toTagDto)
                .toList();
        List<RatingDto> ratings = recipe.getRatings()
                .stream()
                .map(ModelConverter::toRatingDto)
                .toList();
        List<AllergyDto> allergies = recipe.getAllergies()
                .stream()
                .map(ModelConverter::toAllergyDto)
                .toList();
        return RecipeDto.builder()
                .id(recipe.getId())
                .tags(tags)
                .averageRating(recipe.getAverageRating())
                .imageUrl(recipe.getImageUrl())
                .difficulty(recipe.getDifficulty())
                .cookTimeMinutes(recipe.getCookTimeMinutes())
                .prepTimeMinutes(recipe.getPrepTimeMinutes())
                .ratings(ratings)
                .estimatedCalories(recipe.getEstimatedCalories())
                .instructions(recipe.getInstructions())
                .createdAt(recipe.getCreatedAt())
                .updatedAt(recipe.getUpdatedAt())
                .name(recipe.getName())
                .ingredients(ingredients)
                .nutritionist(recipe.getNutritionist().getUsername())
                .allergies(allergies)
                .build();

    }

    private static RatingDto toRatingDto(Rating rating) {
        if (rating == null) {
            return null;
        }
        return RatingDto.builder()
                .username(rating.getUser().getUsername())
                .recipeId(rating.getRecipe().getId())
                .value(rating.getValue())
                .build();
    }

    private static IngredientDto toIngredientDto(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }
        return IngredientDto.builder()
                .name(ingredient.getName())
                .id(ingredient.getId())
                .recipes(ingredient.getRecipes())
                .build();
    }

    public static AddRecipeResponse toAddRecipeResponse(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        List<IngredientDto> ingredients = recipe.getIngredients()
                .stream()
                .map(ModelConverter::toIngredientDto)
                .toList();
        return new AddRecipeResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getNutritionist().getUsername(),
                ingredients);
    }

    private static TagDto toTagDto(Tag tag) {
        if (tag == null) {
            return null;
        }
        return new TagDto(tag.getName());
    }

    public static RecipesByNutritionistResponse toRecipesByNutritionistResponse(String username, List<Recipe> recipes) {
        if (username == null || recipes == null) {
            return null;
        }
        List<RecipeDto> recipesDto = recipes.stream()
                .map(ModelConverter::toRecipeDto)
                .toList();
        return new RecipesByNutritionistResponse(username, recipesDto);
    }

    public static Profile toProfile(ProfileDto profileDto) {
        if (profileDto == null) {
            return null;
        }
        List<Allergy> allergies = profileDto.allergies()
                .stream()
                .map(ModelConverter::toAllergy)
                .toList();
        Profile profile = new Profile();
        profile.setId(profile.getId());
        profile.setUser(profile.getUser());
        profile.setDietaryPreference(profileDto.dietaryPreference());
        profile.setAllergies(allergies);
        return profile;
    }

    public static Profile toProfile(CreateProfileRequest data) {
        if (data == null) {
            return null;
        }
        List<Allergy> allergies = data.allergies()
                .stream()
                .map(ModelConverter::toAllergy)
                .toList();
        Profile profile = new Profile();
        profile.setId(profile.getId());
        profile.setUser(profile.getUser());
        profile.setDietaryPreference(data.dietaryPreference());
        profile.setAllergies(allergies);
        return profile;
    }

    public static Allergy toAllergy(AllergyDto allergyDto) {
        if (allergyDto == null) {
            return null;
        }
        Allergy allergy = new Allergy();
        allergy.setName(allergyDto.name());
        allergy.setDescription(allergyDto.description());
        allergy.setId(allergy.getId());
        return allergy;
    }
    public static Allergy toAllergy(AllergyType allergyType) {
        if (allergyType == null) {
            return null;
        }
        Allergy allergy = new Allergy();
        allergy.setName(allergyType.name());
        allergy.setDescription(allergyType.getDescription());
        allergy.setId(allergy.getId());
        return allergy;
    }


    public static ProfileDto toProfileDto(Profile profile) {
        if (profile == null) {
            return null;
        }
        List<AllergyDto> allergies = profile.getAllergies()
                .stream()
                .map(ModelConverter::toAllergyDto)
                .toList();
        return ProfileDto.builder()
                .id(profile.getId())
                .userId(profile.getId())
                .dietaryPreference(profile.getDietaryPreference())
                .allergies(allergies)
                .build();

    }

    public static RegularUserDto toRegularUserDto(RegularUser user) {
        if (user == null) {
            return null;
        }
        List<AuthorityDto> authorities = user.getAuthorities()
                .stream()
                .map(ModelConverter::toAuthority)
                .toList();
        return RegularUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .authorities(authorities)
                .accountNonExpired(user.isAccountNonExpired())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .profileDto(ModelConverter.toProfileDto(user.getProfile()))
                .password(user.getPassword())
                .updatedAt(user.getUpdatedAt())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private static AuthorityDto toAuthority(Authority authority) {
        if (authority == null) {
            return null;
        }
        return AuthorityDto.builder()
                .id(authority.getId())
                .user(ModelConverter.toUserDto(authority.getUser()))
                .role(authority.getRole())
                .build();
    }

    private static UserDto toUserDto(User user) {
        //TODO add logic
        return null;
    }

    private static AllergyDto toAllergyDto(Allergy allergy) {
        if (allergy == null) {
            return null;
        }
        return new AllergyDto.Builder()
                .id(allergy.getId())
                .type(allergy.getName())
                .description(allergy.getDescription())
                .build();
    }

    public static Tag toTag(TagDto tagDto) {
        if (tagDto == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setName(tagDto.name());
        return tag;
    }

    public static Tag toTag(TagType tagType) {
        if (tagType == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setName(tagType.name());
        return tag;
    }
}
