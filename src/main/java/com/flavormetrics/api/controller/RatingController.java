package com.flavormetrics.api.controller;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.RatingDto;
import com.flavormetrics.api.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rating")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/{recipeId}")
    public ResponseEntity<Data<String>> addRating(
            @PathVariable UUID recipeId,
            @RequestBody Data<Integer> requestBody,
            Authentication authentication) {
        return ResponseEntity.ok(ratingService.addRecipeRating(recipeId, requestBody.data(), authentication));
    }

    @GetMapping("/{recipeId}/all")
    public ResponseEntity<Data<List<RatingDto>>> getAllRatingsByRecipeId(@PathVariable UUID recipeId) {
        return ResponseEntity.ok(ratingService.getAllRatingsByRecipeId(recipeId));
    }

    @GetMapping("/byUser")
    public ResponseEntity<Data<List<RatingDto>>> getAllRatingsByRecipeId(Authentication authentication) {
        return ResponseEntity.ok(ratingService.getAllRatingsByUser(authentication));
    }
}