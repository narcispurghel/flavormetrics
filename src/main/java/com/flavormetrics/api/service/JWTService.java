package com.flavormetrics.api.service;

import java.util.UUID;

import com.flavormetrics.api.entity.user.User;
import com.nimbusds.jose.jwk.RSAKey;

public interface JWTService {
    String generateToken(User details);

    UUID getId();

    RSAKey getPublicKey();

    boolean isTokenValid(String token);

    String extractUsername(String token);
}
