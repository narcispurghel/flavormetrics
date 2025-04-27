package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.user.impl.Nutritionist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NutritionistRepository extends JpaRepository<Nutritionist, UUID> {
}
