package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.Authority;
import com.flavormetrics.api.enums.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    void testIf_findAuthorityByType_ReturnsNotEmpty() {
        var authority = new Authority(RoleType.ROLE_USER);
        authorityRepository.save(authority);
        Optional<Authority> result = authorityRepository.findAuthorityByType(RoleType.ROLE_USER);
        assertTrue(result.isPresent());
        assertEquals(authority, result.get());
    }

    @Test
    void testIf_findAuthorityByType_ReturnsEmpty() {
        Optional<Authority> result = authorityRepository.findAuthorityByType(RoleType.ROLE_USER);
        assertTrue(result.isEmpty());
    }

}