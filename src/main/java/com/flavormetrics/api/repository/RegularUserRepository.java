package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.user.impl.RegularUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface RegularUserRepository extends JpaRepository<RegularUser, UUID> {

    boolean existsByUserDetails_Username_Value(String userDetailsUsernameValue);

    UserDetails findByUserDetails_Username_Value(String userDetailsUsernameValue);
}
