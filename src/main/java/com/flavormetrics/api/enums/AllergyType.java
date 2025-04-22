package com.flavormetrics.api.enums;

public enum AllergyType {
    DAIRY("Allergy to dairy products"),
    EGGS("Allergy to eggs"),
    FISH("Allergy to fish"),
    SHELLFISH("Allergy to shellfish"),
    TREE_NUTS("Allergy to tree nuts"),
    PEANUTS("Allergy to peanuts"),
    WHEAT("Allergy to wheat"),
    SOY("Allergy to soy products"),
    GLUTEN("Allergy or sensitivity to gluten"),
    SESAME("Allergy to sesame seeds"),
    MUSTARD("Allergy to mustard"),
    CELERY("Allergy to celery"),
    SULFITES("Sensitivity to sulfites"),
    LUPIN("Allergy to lupin");

    private final String description;

    AllergyType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

