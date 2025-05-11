package com.flavormetrics.api.service;

import com.flavormetrics.api.entity.user.User;
import com.nimbusds.jose.jwk.RSAKey;

import java.util.UUID;

public interface JwtService {
    String generateToken(User details);

    UUID getId();

    RSAKey getPublicKey();

    boolean isTokenValid(String token);

    String extractUsername(String token);
}
