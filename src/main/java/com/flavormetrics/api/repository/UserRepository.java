package com.flavormetrics.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flavormetrics.api.entity.User;

public interface UserRepository extends JpaRepository<User, UUID>{

    boolean existsByEmail(String email);

}
