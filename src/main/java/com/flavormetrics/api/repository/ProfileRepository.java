package com.flavormetrics.api.repository;

import java.util.Optional;
import java.util.UUID;

import com.flavormetrics.api.model.projection.ProfileProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.flavormetrics.api.entity.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    @Query("""
            SELECT p
            FROM Profile p
            LEFT JOIN FETCH p.allergies
            LEFT JOIN FETCH p.user u
            WHERE u.id = ?1
            """)
    Optional<Profile> findByIdUserId(UUID id);
}
