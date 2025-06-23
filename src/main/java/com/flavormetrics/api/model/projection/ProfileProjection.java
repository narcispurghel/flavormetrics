package com.flavormetrics.api.model.projection;

import com.flavormetrics.api.model.enums.DietaryPreferenceType;

import java.util.Set;
import java.util.UUID;

public interface ProfileProjection {

        UUID getId();

        DietaryPreferenceType getDietaryPreference();

        Set<String> getAllergies();

}
