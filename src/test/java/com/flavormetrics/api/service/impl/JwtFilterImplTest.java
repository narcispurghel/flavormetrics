package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.Authority;
import com.flavormetrics.api.entity.Jwt;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.model.enums.RoleType;
import com.flavormetrics.api.repository.JWTRepository;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class JwtFilterImplTest {
    private static final List<Authority> AUTHORITIES = List.of(
            new Authority(RoleType.ROLE_USER));
    private static final String USERNAME = "test@email.com";
    private static final UUID JWT_ID = UUID.randomUUID();

    @Mock
    private User user;
    @Mock
    private JWTRepository jwtRepository;

    private JwtServiceImpl jwtServiceImpl;

    @BeforeEach
    void init() {
        jwtServiceImpl = new JwtServiceImpl(jwtRepository);
    }

    @Test
    void testThatTokenIsGenerated() {
        final Jwt jwtEntity = new Jwt();
        jwtEntity.setId(JWT_ID);
        Mockito.when(user.getAuthorities())
                .thenReturn(AUTHORITIES);
        Mockito.when(user.getUsername())
                .thenReturn(USERNAME);
        Mockito.when(jwtRepository.save(any()))
                .thenReturn(jwtEntity);
        try (MockedConstruction<SignedJWT> ignored = Mockito.mockConstruction(SignedJWT.class,
                (mock, context) -> Mockito.when(mock.serialize()).thenReturn("mockedJwt"))) {
            final String jwt = jwtServiceImpl.generateToken(user);
            assertEquals("mockedJwt", jwt);
        }
    }
}
