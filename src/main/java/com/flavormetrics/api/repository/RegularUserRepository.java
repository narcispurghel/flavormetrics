package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.entity.user.impl.RegularUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RegularUserRepository extends JpaRepository<RegularUser, UUID> {
    User findByUsername_Value(String usernameValue);

    boolean existsByUsername_Value(String usernameValue);

    Optional<RegularUser> getByUsername_Value(String usernameValue);
}
