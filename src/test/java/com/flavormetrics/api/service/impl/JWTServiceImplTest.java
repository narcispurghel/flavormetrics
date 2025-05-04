package com.flavormetrics.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flavormetrics.api.entity.Authority;
import com.flavormetrics.api.entity.JWT;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.model.enums.RoleType;
import com.flavormetrics.api.repository.JWTRepository;

@ExtendWith(MockitoExtension.class)
class JWTServiceImplTest {
        private static final List<Authority> AUTHORITIES = List.of(
                        new Authority(RoleType.ROLE_USER));
        private static final String USERNAME = "test@email.com";
        private static final UUID JWT_ID = UUID.randomUUID();

        @Mock
        private User user;
        @Mock
        private JWTRepository jwtRepository;
        private JWTServiceImpl jwtServiceImpl;

        @BeforeEach
        void init() {
                jwtServiceImpl = new JWTServiceImpl(jwtRepository);
        }

        @Test
        void testThatTokenIsGenerated() {
                final JWT jwtEntity = new JWT();
                jwtEntity.setId(JWT_ID);
                Mockito.when(user.getAuthorities())
                                .thenReturn(AUTHORITIES);
                Mockito.when(user.getUsername())
                                .thenReturn(USERNAME);
                Mockito.when(jwtRepository.save(any()))
                                .thenReturn(jwtEntity);
                final String jwt = jwtServiceImpl.generateToken(user);
                assertNotNull(jwt);
                assertFalse(jwt::isBlank);
        }
}
