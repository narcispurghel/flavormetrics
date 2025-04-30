package com.flavormetrics.api.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record UserDto(
        @JsonIgnore UUID id,
        String email,
        String firstName,
        String lastName) {

}
