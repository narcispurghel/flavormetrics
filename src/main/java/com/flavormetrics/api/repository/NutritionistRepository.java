package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NutritionistRepository extends JpaRepository<Nutritionist, UUID> {

    User findByUsername_Value(String username);

    boolean existsByUsername_Value(String email);

    Optional<Nutritionist> getByUsername_Value(String usernameValue);
}
