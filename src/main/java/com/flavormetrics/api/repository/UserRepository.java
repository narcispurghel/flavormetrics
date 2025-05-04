package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername_Value(String username);

    Optional<User> findByUsername_Value(String usernameValue);

    User getByUsername_Value(String email);
}
