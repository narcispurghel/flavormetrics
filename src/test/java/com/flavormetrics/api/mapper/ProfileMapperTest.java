package com.flavormetrics.api.mapper;

import com.flavormetrics.api.entity.Allergy;
import com.flavormetrics.api.entity.Profile;
import com.flavormetrics.api.entity.User;
import com.flavormetrics.api.model.ProfileDto;
import com.flavormetrics.api.enums.DietaryPreferenceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ProfileMapperTest {
    private Profile profile;

    @BeforeEach
    void setUp() {
        var user = new  User();
        user.setId(UUID.randomUUID());
        profile = new Profile();
        profile.setId(UUID.randomUUID());
        profile.setBio("mock-bio");
        profile.setUser(user);
        profile.setDietaryPreference(DietaryPreferenceType.fish_inclusive);
        profile.setAllergies(Set.of(new Allergy("mock-allergy")));
    }

    @Test
    void testIf_toDto_MapsCorrectly() {
        ProfileDto result =  ProfileMapper.toDto(profile);
        assertNotNull(result);
        assertEquals(profile.getBio(), result.bio());
        assertEquals(profile.getId(), result.id());
        assertEquals(profile.getUser().getId(), result.userId());
        assertEquals(profile.getDietaryPreference(), result.dietaryPreference());
        assertEquals(profile.getAllergies().size(), result.allergies().size());
    }
}