package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.Profile;
import com.flavormetrics.api.model.ProfileDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Optional<ProfileDto> findByUser_Username_Value(String userUsernameValue);

    boolean existsByUser_Username_Value(String userUsernameValue);
}
