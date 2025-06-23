package com.flavormetrics.api.repository;

import com.flavormetrics.api.model.enums.DietaryPreferenceType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID EMAIL_ID = UUID.randomUUID();
    private static final UUID PROFILE_ID = UUID.randomUUID();
    private static final String USER_FIRST_NAME = "test_first_name";
    private static final String USER_LAST_NAME = "test_last_name";
    private static final String USER_EMAIL = "test_email";
    private static final String USER_PASSWORD = "test_password";
    private static final String DIETARY_PREFERENCE_TYPE = DietaryPreferenceType.VEGAN.name();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testIfHasProfileReturnsTrue() {
        entityManager.createNativeQuery("""
                        INSERT INTO emails (id, address)
                        VALUES (?, ?)
                        """)
                .setParameter(1, EMAIL_ID)
                .setParameter(2, USER_EMAIL)
                .executeUpdate();
        entityManager.createNativeQuery("""
                        INSERT INTO users (id, first_name, last_name, email_id, password_hash)
                        VALUES (?, ?, ?, ?, ?)
                        """)
                .setParameter(1, USER_ID)
                .setParameter(2, USER_FIRST_NAME)
                .setParameter(3, USER_LAST_NAME)
                .setParameter(4, EMAIL_ID)
                .setParameter(5, USER_PASSWORD)
                .executeUpdate();
        entityManager.createNativeQuery("""
                        INSERT INTO profiles (id, user_id, dietary_preference)
                        values (?, ?, ?)
                        """)
                .setParameter(1, PROFILE_ID)
                .setParameter(2, USER_ID)
                .setParameter(3, DIETARY_PREFERENCE_TYPE)
                .executeUpdate();
        entityManager.clear();
        assertTrue(userRepository.hasProfile(USER_ID));
    }

    @Test
    void testIfHasProfileReturnsFalse() {
        assertFalse(userRepository.hasProfile(USER_ID));
    }

}