package com.flavormetrics.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flavormetrics.api.entity.Allergy;
import java.util.UUID;

public record AllergyDto(@JsonIgnore UUID id, String name, String description) {
  public AllergyDto(Allergy allergy) {
    this(allergy.getId(), allergy.getName(), allergy.getDescription());
  }
}
