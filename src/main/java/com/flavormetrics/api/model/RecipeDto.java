package com.flavormetrics.api.model;

import com.flavormetrics.api.model.enums.DifficultyType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record RecipeDto(
        UUID id,
        String name,
        String nutritionist,
        String instructions,
        String imageUrl,
        Integer prepTimeMinutes,
        Integer cookTimeMinutes,
        DifficultyType difficulty,
        Integer estimatedCalories,
        Double averageRating,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<TagDto> tags,
        List<IngredientDto> ingredients,
        List<RatingDto> ratings
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String nutritionist;
        private String instructions;
        private String imageUrl;
        private Integer prepTimeMinutes;
        private Integer cookTimeMinutes;
        private DifficultyType difficulty;
        private Integer estimatedCalories;
        private Double averageRating;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<TagDto> tags;
        private List<IngredientDto> ingredients;
        private List<RatingDto> ratings;

        private Builder() {
            // Prevent instantiation
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder nutritionist(String nutritionist) {
            this.nutritionist = nutritionist;
            return this;
        }

        public Builder instructions(String instructions) {
            this.instructions = instructions;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder prepTimeMinutes(Integer prepTimeMinutes) {
            this.prepTimeMinutes = prepTimeMinutes;
            return this;
        }

        public Builder cookTimeMinutes(Integer cookTimeMinutes) {
            this.cookTimeMinutes = cookTimeMinutes;
            return this;
        }

        public Builder difficulty(DifficultyType difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Builder estimatedCalories(Integer estimatedCalories) {
            this.estimatedCalories = estimatedCalories;
            return this;
        }

        public Builder averageRating(Double averageRating) {
            this.averageRating = averageRating;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder tags(List<TagDto> tags) {
            this.tags = new ArrayList<>(tags);
            return this;
        }

        public Builder ingredients(List<IngredientDto> ingredients) {
            this.ingredients = new ArrayList<>(ingredients);
            return this;
        }

        public Builder ratings(List<RatingDto> ratings) {
            this.ratings = ratings;
            return this;
        }

        public RecipeDto build() {
            return new RecipeDto(id,
                    name,
                    nutritionist,
                    instructions,
                    imageUrl,
                    prepTimeMinutes,
                    cookTimeMinutes,
                    difficulty,
                    estimatedCalories,
                    averageRating,
                    createdAt,
                    updatedAt,
                    tags,
                    ingredients,
                    ratings);
        }
    }
}
