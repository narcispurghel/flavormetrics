package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.Rating;
import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.exception.MaximumNumberOfRatingException;
import com.flavormetrics.api.exception.RecipeNotFoundException;
import com.flavormetrics.api.model.RatingDto;
import com.flavormetrics.api.model.UserDetailsImpl;
import com.flavormetrics.api.repository.RatingRepository;
import com.flavormetrics.api.repository.RecipeRepository;
import com.flavormetrics.api.repository.UserRepository;
import com.flavormetrics.api.service.RatingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
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
    @Transactional
    public Map<String, String> addRecipeRating(UUID recipeId, int ratingValue) {
        Recipe recipe;
        try {
            recipe = recipeRepository.getReferenceById(recipeId);
        } catch (EntityNotFoundException e) {
            throw new RecipeNotFoundException();
        }
        var principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isRated = ratingRepository.isRecipeAlreadyRatedByUser(principal.id(), recipeId);
        if (isRated) {
            throw new MaximumNumberOfRatingException();
        }
        Rating rating = new Rating();
        rating.setUser(userRepository.getReferenceById(principal.id()));
        rating.setRecipe(recipe);
        rating.setScore(ratingValue);
        recipe.getRatings().add(rating);
        ratingRepository.save(rating);
        recipeRepository.save(recipe);
        return Map.of("message", "Recipe has been rated");
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RatingDto> findAllRatingsByRecipeId(UUID recipeId) {
        return ratingRepository.findAllByRecipeId(recipeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RatingDto> findAllRatingsByUserId(UUID userId) {
        return ratingRepository.findAllRatingsByUserId(userId);
    }
}