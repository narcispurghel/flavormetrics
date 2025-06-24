package com.flavormetrics.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flavormetrics.api.entity.Tag;
import org.springframework.lang.NonNull;

import java.util.UUID;

public record TagDto(
        @JsonIgnore
        UUID id,
        String name) {

    public TagDto(@NonNull Tag tag) {
        this(tag.getId(), tag.getName());
    }

}
