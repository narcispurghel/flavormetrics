package com.flavormetrics.api.service;

import com.nimbusds.jose.jwk.RSAKey;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface JWTService {
    String generateToken(UserDetails details);

    @Transactional
    UUID getId();

    RSAKey getPublicKey();

    boolean isTokenValid(String token);

    String extractUsername(String token);
}
