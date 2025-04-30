package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.Authority;
import com.flavormetrics.api.model.enums.RoleType;
import com.flavormetrics.api.repository.JWTRepository;
import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class JWTServiceImplTest {
    private final String USERNAME = "test";
    private final List<Authority> AUTHORITIES = List.of(
            new Authority.Builder(RoleType.ROLE_USER)
                    .build());

    @Mock
    private JWTRepository jwtRepository;

    @Mock
    private UserDetails userDetails;

    private JWTServiceImpl jwtService;

    @BeforeEach
    void init() throws JOSEException {
        jwtService = new JWTServiceImpl(jwtRepository);
    }

    @Test
    void testIfTokenIsGenerated() {
        Mockito.when(userDetails.getUsername())
                .thenReturn(USERNAME);
        Mockito.when(userDetails.getAuthorities())
                .thenReturn(new ArrayList<>());

    }
}