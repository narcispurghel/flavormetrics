package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.RecipeDto;
import com.flavormetrics.api.model.enums.DifficultyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    Page<Recipe> getAllByProfileFilters(List<String> allergies, String dietaryPreferences, Pageable pageable);

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
