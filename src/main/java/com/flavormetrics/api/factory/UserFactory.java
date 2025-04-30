package com.flavormetrics.api.factory;

import com.flavormetrics.api.entity.Authority;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.entity.user.impl.RegularUser;
import com.flavormetrics.api.model.enums.RoleType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserFactory {
    private final PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User getUser(RoleType role, String username, String firstName,
                        String lastName, String password) {
        return switch (role) {
            case ROLE_ADMIN -> null;
            case ROLE_USER -> createRegularUser(role, username, firstName, lastName, password);
            case ROLE_NUTRITIONIST -> createNutritionistUser(role, username, firstName, lastName, password);
        };
    }

    private User createNutritionistUser(RoleType role, String username, String firstName,
                                        String lastName, String password) {
        Authority authority = new Authority.Builder(role)
                .build();
        List<Authority> authorities = List.of(authority);
        return new Nutritionist.Builder(passwordEncoder.encode(password), username, role)
                .firstName(firstName)
                .lastName(lastName)
                .updatedAt(LocalDateTime.now())
                .authorities(authorities)
                .build();
    }

    private RegularUser createRegularUser(RoleType role, String username, String firstName,
                                          String lastName, String password) {
        Authority authority = new Authority.Builder(role)
                .build();
        List<Authority> authorities = List.of(authority);
        return new RegularUser.Builder(passwordEncoder.encode(password), username, role)
                .firstName(firstName)
                .lastName(lastName)
                .updatedAt(LocalDateTime.now())
                .authorities(authorities)
                .build();
    }
}
