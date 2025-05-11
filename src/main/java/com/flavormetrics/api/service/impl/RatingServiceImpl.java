package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.Rating;
import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.exception.MaximumNumberOfRatingException;
import com.flavormetrics.api.exception.impl.InvalidArgumentException;
import com.flavormetrics.api.exception.impl.RecipeNotFoundException;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.RatingDto;
import com.flavormetrics.api.repository.RatingRepository;
import com.flavormetrics.api.repository.RecipeRepository;
import com.flavormetrics.api.repository.UserRepository;
import com.flavormetrics.api.service.RatingService;
import com.flavormetrics.api.util.ModelConverter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RatingServiceImpl implements RatingService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final RatingRepository ratingRepository;

    public RatingServiceImpl(UserRepository userRepository,
                             RecipeRepository recipeRepository,
                             RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Data<String> addRecipeRating(UUID recipeId, int ratingValue, Authentication authentication) {
        if (ratingValue < 0 || ratingValue > 5) {
            throw new InvalidArgumentException(
                    "Invalid ratingValue",
                    "Rating must be in interval [0, 5]",
                    HttpStatus.BAD_REQUEST,
                    "data.ratingValue");
        }
        final Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(
                        "Invalid recipe id",
                        "Cannot find a recipe associated with id " + recipeId,
                        HttpStatus.BAD_REQUEST,
                        "id"));
        final User user = userRepository.findByUsername_Value(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Cannot find a user associated with username " + authentication.getName()));
        final boolean userCannotRate = isRecipeAlreadyRatedByUser(user, recipeId);
        if (userCannotRate) {
            throw new MaximumNumberOfRatingException(
                    "User already rated this recipe",
                    "User cannot rate a recipe multiple times",
                    HttpStatus.BAD_REQUEST,
                    "user.ratings"
            );
        }
        final Rating rating = new Rating();
        rating.setUser(user);
        rating.setRecipe(recipe);
        rating.setValue(ratingValue);
        recipe.getRatings().add(rating);
        user.getRatings().add(rating);
        ratingRepository.save(rating);
        recipeRepository.save(recipe);
        userRepository.save(user);
        return Data.body("Rating " + ratingValue + " was added successfully to recipe " + recipe.getId());
    }

    @Override
    public Data<List<RatingDto>> getAllRatingsByRecipeId(UUID recipeId) {
        List<Rating> ratings = ratingRepository.findAllByRecipe_Id(recipeId);
        List<RatingDto> ratingsDto = ratings.stream()
                .map(ModelConverter::toRatingDto)
                .toList();
        return Data.body(ratingsDto);
    }

    @Override
    public Data<List<RatingDto>> getAllRatingsByUser(Authentication authentication) {
        List<Rating> ratings = ratingRepository.findAllByUser_Username_Value(authentication.getName());
        List<RatingDto> ratingsDto = ratings.stream()
                .map(ModelConverter::toRatingDto)
                .toList();
        return Data.body(ratingsDto);
    }

    private boolean isRecipeAlreadyRatedByUser(User user, UUID recipeId) {
        return user.getRatings()
                .stream()
                .anyMatch(rating -> rating.getRecipe().getId().equals(recipeId));
    }
}