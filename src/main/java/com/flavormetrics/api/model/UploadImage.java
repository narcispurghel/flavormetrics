package com.flavormetrics.api.model;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record UploadImage(@URL String url, @NotBlank String name) {}
