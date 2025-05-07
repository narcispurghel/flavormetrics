package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.Profile;
import com.flavormetrics.api.entity.user.impl.RegularUser;
import com.flavormetrics.api.model.ProfileDto;
import com.flavormetrics.api.exception.impl.ProfileAlreadyCreatedException;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.RecipeDto;
import com.flavormetrics.api.repository.ProfileRepository;
import com.flavormetrics.api.repository.RegularUserRepository;
import com.flavormetrics.api.service.ProfileService;
import com.flavormetrics.api.util.ModelConverter;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final RegularUserRepository regularUserRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository,
                              RegularUserRepository regularUserRepository) {
        this.profileRepository = profileRepository;
        this.regularUserRepository = regularUserRepository;
    }

    @Override
    @Transactional
    public Data<ProfileDto> getProfile(Authentication authentication) {
        Profile profile = profileRepository.findByUser_Username_Value(authentication.getName())
                .orElse(null);
        return Data.body(ModelConverter.toProfileDto(profile));
    }

    @Override
    @Transactional
    public Data<ProfileDto> createProfile(ProfileDto profile, Authentication authentication) {
        if (profile == null) {
            return null;
        }
        boolean exists = profileRepository.existsByUser_Username_Value(authentication.getName());
        if (exists) {
            throw new ProfileAlreadyCreatedException(
                    "Bad request", "User profile exists", HttpStatus.BAD_REQUEST, "profile");
        }
        RegularUser regularUser = regularUserRepository.getByUsername_Value(authentication.getName())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Cannot find a user account associatted with username " + authentication.getName()));
        Profile newProfile = ModelConverter.toProfile(profile);
        newProfile.setUser(regularUser);
        newProfile = profileRepository.save(newProfile);
        regularUser.setProfile(newProfile);
        regularUserRepository.save(regularUser);
        return Data.body(ModelConverter.toProfileDto(newProfile));
    }
}