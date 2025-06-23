package com.flavormetrics.api.repository;

import java.util.Optional;
import java.util.UUID;

import com.flavormetrics.api.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.flavormetrics.api.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
    Optional<Authority> getAuthorityByType(RoleType roleType);
}
