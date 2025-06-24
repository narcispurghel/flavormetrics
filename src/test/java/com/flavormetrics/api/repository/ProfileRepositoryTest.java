package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.Email;
import com.flavormetrics.api.entity.Profile;
import com.flavormetrics.api.entity.User;
import com.flavormetrics.api.model.enums.DietaryPreferenceType;
import com.flavormetrics.api.model.projection.ProfileProjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testIf_findProfileById_ReturnsProfileProjection() {
        Profile profile = new Profile();
        profile.setDietaryPreference(DietaryPreferenceType.VEGAN);
        profile = profileRepository.save(profile);
        Optional<ProfileProjection> found = profileRepository.findProfileProjectionById(profile.getId());
        assertTrue(found.isPresent());
        assertEquals(profile.getId(), found.get().getId());
        assertEquals(DietaryPreferenceType.VEGAN, found.get().getDietaryPreference());
    }

    @Test
    void testIf_findProfileProjectionById_ReturnsEmpty() {
        UUID randomId = UUID.randomUUID();
        Optional<ProfileProjection> found = profileRepository.findProfileProjectionById(randomId);
        assertFalse(found.isPresent());
    }

    @Test
    void testIf_findByUserId_ReturnsNotEmpty() {
        var profile = new Profile();
        var user = new User();
        var email = new Email();
        email.setAddress("mock-email-address");
        user.setEmail(email);
        user.setFirstName("mock-first-name");
        user.setLastName("mock-last-name");
        user.setPasswordHash("mock-password-hash");
        user = userRepository.save(user);
        profile.setUser(user);
        profileRepository.save(profile);
        Optional<Profile> result = profileRepository.findByIdUserId(user.getId());
        assertTrue(result.isPresent());
        assertEquals(profile, result.get());
        assertEquals(user, result.get().getUser());
    }

    @Test
    void testIf_findByUserId_ReturnsEmpty() {
        Optional<Profile> result = profileRepository.findByIdUserId(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }
}
