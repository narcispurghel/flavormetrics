package com.flavormetrics.api.factory;

import com.flavormetrics.api.entity.Authority;
import com.flavormetrics.api.entity.UserDetails;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.entity.user.impl.RegularUser;
import com.flavormetrics.api.exception.impl.InvalidArgumentException;
import com.flavormetrics.api.model.enums.RoleType;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
            default -> throw new InvalidArgumentException(
                    "Invalid role",
                    "role should be value from RoleType",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "user.role"
            );
        };
    }

    private User createNutritionistUser(RoleType role, String username, String firstName,
                                        String lastName, String password) {
        Authority authority = new Authority.Builder(role)
                .build();
        UserDetails details = new UserDetails.Builder(passwordEncoder.encode(password), username, role)
                .build();
        details.getAuthorities().add(authority);

        User user = new Nutritionist.Builder()
                .build();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserDetails(details);
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }

    private RegularUser createRegularUser(RoleType role, String username, String firstName,
                                          String lastName, String password) {
        Authority authority = new Authority.Builder(role)
                .build();
        UserDetails details = new UserDetails.Builder(passwordEncoder.encode(password), username, role)
                .build();
        details.getAuthorities().add(authority);

        RegularUser user = new RegularUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserDetails(details);
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }
}
