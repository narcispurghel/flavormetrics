package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.Ingredient;
import com.flavormetrics.api.model.IngredientDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {

    @Query("""
            SELECT new com.flavormetrics.api.model.IngredientDto(i.id, i.name)
            FROM Ingredient i
            WHERE i.name IN (?1)
            """)
    List<IngredientDto> getIdsAndNames(List<String> names);

}
