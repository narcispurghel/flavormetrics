package com.flavormetrics.api.model.request;

import com.flavormetrics.api.model.AllergyDto;
import com.flavormetrics.api.model.enums.AllergyType;
import com.flavormetrics.api.model.enums.DietaryPreferenceType;

import java.util.Set;
import java.util.UUID;

public record CreateProfileRequest(
        DietaryPreferenceType dietaryPreference,
        Set<AllergyDto> allergies
) {}
