package com.flavormetrics.api.enums;

public enum DietaryPreferenceType {
  vegetarian("No meat, may include dairy/eggs"),
  vegan("No animal products at all"),
  fish_inclusive("Includes fish but no other meat"),
  keto("Low-carb, high-fat diet"),
  paleo("Focus on whole foods, excludes grains, dairy, processed foods"),
  low_carb("General reduction in carbohydrate intake"),
  low_fat("Reduces fat consumption"),
  halal("Follows Islamic dietary laws"),
  kosher("Follows Jewish dietary laws"),
  diabetic_friendly("Tailored to blood sugar control"),
  none("No dietary restrictions"),
  high_protein("Focus on great amount of proteins"),
  high_fiber("Focus on great amount of proteins");

  private final String description;

  DietaryPreferenceType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
