package com.flavormetrics.api.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum UserRole {
    USER,
    NUTRITIONIST,
    ADMIN;

    public static List<String> names() {
        List<String> names = new ArrayList<>();

        for (UserRole role : UserRole.values()) {
            names.add(role.name());
        }

        return names;
    }
}
