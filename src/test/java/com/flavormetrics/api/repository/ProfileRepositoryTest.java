package com.flavormetrics.api.repository;

import com.flavormetrics.api.model.projection.ProfileProjection;
import com.flavormetrics.api.model.enums.DietaryPreferenceType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProfileRepositoryTest {
    private static final UUID PROFILE_ID = UUID.randomUUID();
    private static final String DIETARY_PREFERENCE_TYPE = DietaryPreferenceType.VEGAN.name();

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testIfFindProfileByIdReturnsProfileProjectionProjection() {
        entityManager.createNativeQuery("""
                        INSERT INTO profiles (id, dietary_preference)
                        values (?, ?)
                        """)
                .setParameter(1, PROFILE_ID)
                .setParameter(2, DIETARY_PREFERENCE_TYPE)
                .executeUpdate();
        entityManager.clear();
        Optional<ProfileProjection> found = profileRepository.findProfileProjectionById(PROFILE_ID);
        assertTrue(found.isPresent());
        assertEquals(PROFILE_ID, found.get().getId());
        assertEquals(DietaryPreferenceType.valueOf(DIETARY_PREFERENCE_TYPE), found.get().getDietaryPreference());
    }

    @Test
    void testIfFindProfileProjectionByIdReturnsEmptyOptional() {
        Optional<ProfileProjection> found = profileRepository.findProfileProjectionById(PROFILE_ID);
        assertFalse(found.isPresent());
    }

}