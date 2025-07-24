package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.Authority;
import com.flavormetrics.api.enums.RoleType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
  Optional<Authority> findAuthorityByType(RoleType roleType);
}
