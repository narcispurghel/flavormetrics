package com.flavormetrics.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flavormetrics.api.entity.Tag;
import java.util.UUID;
import org.springframework.lang.NonNull;

public record TagDto(@JsonIgnore UUID id, String name) {
  public TagDto(@NonNull Tag tag) {
    this(tag.getId(), tag.getName());
  }
}
