package com.flavormetrics.api.service;

import com.flavormetrics.api.model.ProfileDto;
import com.flavormetrics.api.model.request.CreateProfileRequest;

import java.util.UUID;

public interface ProfileService {

    ProfileDto findById(UUID id);

    UUID create(CreateProfileRequest request);

    ProfileDto updateById(CreateProfileRequest request);

    void remove();
}
