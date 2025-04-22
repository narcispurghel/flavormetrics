package com.flavormetrics.api.enums;

public enum DietaryPreferenceType {
    VEGETARIAN("No meat, may include dairy/eggs"),
    VEGAN("No animal products at all"),
    PESCATARIAN("Includes fish but no other meat"),
    KETO("Low-carb, high-fat diet"),
    PALEO("Focus on whole foods, excludes grains, dairy, processed foods"),
    LOW_CARB("General reduction in carbohydrate intake"),
    LOW_FAT("Reduces fat consumption"),
    HALAL("Follows Islamic dietary laws"),
    KOSHER("Follows Jewish dietary laws"),
    DIABETIC_FRIENDLY("Tailored to blood sugar control"),
    NONE("No dietary restrictions");


    private final String description;

    DietaryPreferenceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
