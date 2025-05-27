package com.flavormetrics.api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.model.enums.DifficultyType;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    Optional<Recipe> getRecipeById(UUID id);

    List<Recipe> getRecipesByNutritionist_Username_Value(String username);


    @Query(value = """
            select r
            from Recipe r
            join r.tags t
            join r.allergies a
            where (a.name is null  or a.name not in :allergies)
            and (:dietaryPreferences is null or t.name in :dietaryPreferences)
            """)
    List<Recipe> getAllByProfileFilters(List<String> allergies, String dietaryPreferences);

    @Query(value = """
            select r
            from Recipe r
            where (r.cookTimeMinutes <= :cookTimeMinutes)
            and (r.estimatedCalories <= :estimatedCalories)
            and (r.prepTimeMinutes <= :prepTimeMinutes)
            and (r.difficulty = :difficulty)
            """)
    List<Recipe> findAllByDefaultFilter(int prepTimeMinutes,
                                                 int cookTimeMinutes,
                                                 int estimatedCalories,
                                                 DifficultyType difficulty);
}
