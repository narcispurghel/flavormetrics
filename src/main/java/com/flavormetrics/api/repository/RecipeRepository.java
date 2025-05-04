package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    Optional<Recipe> getRecipeById(UUID id);

    List<Recipe> getRecipesByNutritionist_Username_Value(String username);
}
