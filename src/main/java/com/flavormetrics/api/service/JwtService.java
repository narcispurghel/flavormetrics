package com.flavormetrics.api.service;

import com.nimbusds.jose.JWSAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;

public interface JwtService {

    String generateToken(UserDetails user);

    SecretKey getSecretKey();

    JWSAlgorithm getAlgorithm();
}
