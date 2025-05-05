package com.flavormetrics.api.model;

import java.util.UUID;

public record RatingDto(
        UUID recipeId,
        String username,
        Integer value
) {
    public static class Builder {
        private UUID recipeId;
        private String username;
        private Integer value;

        public Builder recipeId(UUID recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder value(Integer value) {
            this.value = value;
            return this;
        }

        public RatingDto build() {
            return new RatingDto(recipeId, username, value);
        }
    }
}
