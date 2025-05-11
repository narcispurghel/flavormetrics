package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
    List<Rating> findAllByRecipe_Id(UUID recipeId);

    List<Rating> findAllByUser_Username_Value(String userUsernameValue);
}
