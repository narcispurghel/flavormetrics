package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.Jwt;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.exception.impl.JwtException;
import com.flavormetrics.api.repository.JWTRepository;
import com.flavormetrics.api.service.JwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtServiceImpl.class);
    private static final RSAKey privateKey = generateRSAKey();
    private static final String ISSUER = "flavormetrics";
    private final JWTRepository jwtRepository;

    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

    public JwtServiceImpl(JWTRepository jwtRepository) {
        this.jwtRepository = jwtRepository;
    }

    @Override
    @Transactional
    public String generateToken(User user) {
        if (user == null) {
            throw new JwtException(
                    "Cannot generate token", "User is null", HttpStatus.BAD_REQUEST, "token.user");
        }
        final JWSSigner signer;
        try {
            signer = new RSASSASigner(privateKey);
        } catch (JOSEException e) {
            throw new JwtException(
                    "Invalid private key", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "createToken.signer");
        }
        final String userRole = user.getAuthorities().getFirst().getRole().name();
        final JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .jwtID(getId().toString())
                .expirationTime(getExpirationDate(expirationTime))
                .claim("role", userRole)
                .issuer(ISSUER)
                .issueTime(getIssueDate())
                .subject(user.getUsername())
                .build();
        final JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(privateKey.getKeyID())
                .build();
        final SignedJWT signedJWT = new SignedJWT(header, claims);
        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            throw new JwtException("Invalid signedJWT", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "createToken.signedJWT");
        }
        return signedJWT.serialize();
    }

    @Override
    @Transactional
    public UUID getId() {
        Jwt jwt = new Jwt();
        jwt = jwtRepository.save(jwt);
        LOGGER.info("Getting JWT id: {}", jwt.getId());
        return jwt.getId();
    }

    @Override
    public RSAKey getPublicKey() {
        return privateKey.toPublicJWK();
    }

    @Override
    public boolean isTokenValid(String token) {
        if (token == null) {
            return false;
        }
        final SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new JwtException("Cannot parse JWT", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "isTokenValid.signedJWT");
        }
        final JWSVerifier verifier;
        try {
            verifier = new RSASSAVerifier(getPublicKey());
        } catch (JOSEException e) {
            throw new JwtException("Cannot create JWT verifier", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "isTokenValid.verifier");
        }
        try {
            return signedJWT.verify(verifier)
                    && Objects.nonNull(signedJWT.getHeader())
                    && Objects.nonNull(signedJWT.getPayload())
                    && Objects.nonNull(signedJWT.getJWTClaimsSet())
                    && Objects.nonNull(signedJWT.getJWTClaimsSet().getClaim("role"))
                    && Objects.equals(ISSUER, signedJWT.getJWTClaimsSet().getIssuer())
                    && new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime());
        } catch (JOSEException | ParseException e) {
            throw new JwtException("Cannot verify JWT", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "isTokenValid");
        }
    }

    @Override
    public String extractUsername(String token) {
        if (token == null) {
            return null;
        }
        final SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new JwtException("Cannot parse JWT", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "isTokenValid.signedJWT");
        }
        try {
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new JwtException("Cannot verify JWT", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "isTokenValid");
        }
    }

    private Date getExpirationDate(long expiration) {
        LOGGER.info("JWT expiration time: {}", new Date(Instant.now().toEpochMilli() + expiration));
        return new Date(Instant.now().toEpochMilli() + expiration);
    }

    private Date getIssueDate() {
        return new Date(Instant.now().toEpochMilli());
    }

    private static RSAKey generateRSAKey() {
        String keyId = UUID.randomUUID().toString();
        try {
            return new RSAKeyGenerator(2048)
                    .keyID(keyId)
                    .generate();
        } catch (JOSEException e) {
            throw new JwtException("Key generation failed", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "createToken.signedJWT");
        }
    }
}
