package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.Allergy;
import com.flavormetrics.api.entity.Profile;
import com.flavormetrics.api.entity.User;
import com.flavormetrics.api.exception.ProfileExistsException;
import com.flavormetrics.api.exception.ProfileNotFoundException;
import com.flavormetrics.api.factory.AllergyFactory;
import com.flavormetrics.api.model.AllergyDto;
import com.flavormetrics.api.model.UserDetailsImpl;
import com.flavormetrics.api.enums.AllergyType;
import com.flavormetrics.api.enums.DietaryPreferenceType;
import com.flavormetrics.api.model.projection.ProfileProjection;
import com.flavormetrics.api.model.request.CreateProfileRequest;
import com.flavormetrics.api.repository.ProfileRepository;
import com.flavormetrics.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {
    private static final Set<AllergyDto> allergiesDtos = Set.of(
            new AllergyDto(null, AllergyType.EGGS.name(), AllergyType.EGGS.getDescription())
    );

    private UUID userId;
    private UUID profileId;
    private User user;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AllergyFactory allergyFactory;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        profileId = UUID.randomUUID();
        user = new User();
        user.setId(userId);

        var principal = new UserDetailsImpl(user);
        var auth = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void findById_existingId_returnsProjection() {
        ProfileProjection projection = mock(ProfileProjection.class);
        when(profileRepository.findProfileProjectionById(profileId)).thenReturn(Optional.of(projection));

        ProfileProjection result = profileService.findById(profileId);

        assertEquals(projection, result);
        verify(profileRepository).findProfileProjectionById(profileId);
    }

    @Test
    void findById_notFound_throwsException() {
        when(profileRepository.findProfileProjectionById(profileId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> profileService.findById(profileId));
    }

    @Test
    void create_validRequest_createsProfile() {
        CreateProfileRequest request = new CreateProfileRequest(DietaryPreferenceType.vegan, allergiesDtos);
        Profile profile = new Profile();
        Set<Allergy> allergies = new HashSet<>();
        Profile saved = new Profile();
        saved.setId(profileId);

        when(userRepository.hasProfile(userId)).thenReturn(false);
        when(allergyFactory.checkIfExistsOrElseSave(any())).thenReturn(allergies);
        when(userRepository.getReferenceById(userId)).thenReturn(user);
        when(profileRepository.save(any())).thenReturn(saved);

        UUID result = profileService.create(request);

        assertEquals(profileId, result);
        verify(profileRepository).save(any());
        verify(userRepository).save(user);
    }

    @Test
    void create_alreadyHasProfile_throwsException() {
        when(userRepository.hasProfile(userId)).thenReturn(true);
        CreateProfileRequest request = mock(CreateProfileRequest.class);

        assertThrows(ProfileExistsException.class, () -> profileService.create(request));
    }

    @Test
    void updateById_updatesProfileSuccessfully() {
        Profile profile = new Profile();
        Set<Allergy> allergies = new HashSet<>();
        CreateProfileRequest request = new CreateProfileRequest(DietaryPreferenceType.diabetic_friendly, allergiesDtos);

        when(profileRepository.findByIdUserId(userId)).thenReturn(Optional.of(profile));
        when(allergyFactory.checkIfExistsOrElseSave(any())).thenReturn(allergies);
        when(profileRepository.save(profile)).thenReturn(profile);

        var result = profileService.updateById(request);

        assertEquals(request.dietaryPreference(), result.dietaryPreference());
        verify(profileRepository).save(profile);
    }

    @Test
    void updateById_notFound_throwsException() {
        when(profileRepository.findByIdUserId(userId)).thenReturn(Optional.empty());
        CreateProfileRequest request = new CreateProfileRequest(DietaryPreferenceType.vegan, Set.copyOf(allergiesDtos));

        assertThrows(ProfileNotFoundException.class, () -> profileService.updateById(request));
    }

    @Test
    void remove_deletesProfile() {
        when(userRepository.getProfileId(userId)).thenReturn(Optional.of(profileId));

        profileService.remove();

        verify(profileRepository).deleteById(profileId);
    }

    @Test
    void remove_profileNotFound_throwsException() {
        when(userRepository.getProfileId(userId)).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> profileService.remove());
    }
}

