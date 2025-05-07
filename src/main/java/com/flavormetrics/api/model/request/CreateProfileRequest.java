package com.flavormetrics.api.model.request;

import com.flavormetrics.api.model.enums.AllergyType;
import com.flavormetrics.api.model.enums.DietaryPreferenceType;

import java.util.List;

public record CreateProfileRequest(DietaryPreferenceType dietaryPreference, List<AllergyType> allergies) {
}
