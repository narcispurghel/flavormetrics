package com.flavormetrics.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flavormetrics.api.entity.Tag;
import com.flavormetrics.api.model.enums.TagType;
import org.springframework.lang.NonNull;

import java.util.UUID;

public record TagDto(
        @JsonIgnore
        UUID id,
        TagType name) {

    public TagDto(@NonNull Tag tag) {
        this(tag.getId(), TagType.valueOf(tag.getName()));
    }

}
