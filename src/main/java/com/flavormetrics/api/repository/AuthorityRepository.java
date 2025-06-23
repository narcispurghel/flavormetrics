package com.flavormetrics.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flavormetrics.api.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {


}
