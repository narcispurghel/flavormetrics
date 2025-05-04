package com.flavormetrics.api.service;

import com.flavormetrics.api.model.ProfileDto;
import com.flavormetrics.api.model.Data;
import org.springframework.security.core.Authentication;

public interface ProfileService {
    Data<ProfileDto> getProfile(Authentication authentication);

    Data<ProfileDto> createProfile(ProfileDto profile, Authentication authentication);
}
