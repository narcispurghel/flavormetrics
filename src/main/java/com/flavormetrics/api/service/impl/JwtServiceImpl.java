package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.service.JwtService;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

    @Value("${security.jwt.algorithm}")
    private String algorithm;

    @Override
    public String generateToken(UserDetails userDetails) {
        new Jwt.Builder()
                .claim("username", userDetails.getUsername())
                .expiresAt(expirationTime)
                .issuedAt(LocalDateTime.now())

        return "";
    }

    @Override
    public SecretKey getSecretKey() {

        OctetSequenceKey key = new OctetSequenceKey.Builder(secretKey.getBytes())
                .algorithm(getAlgorithm())
                .build();

        return key.toSecretKey();
    }

    @Override
    public JWSAlgorithm getAlgorithm() {
        return new JWSAlgorithm(algorithm);
    }


}
