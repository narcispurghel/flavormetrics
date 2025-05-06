package com.flavormetrics.api.factory;

import com.flavormetrics.api.entity.Authority;
import com.flavormetrics.api.entity.Email;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.entity.user.impl.Admin;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.entity.user.impl.RegularUser;
import com.flavormetrics.api.model.request.RegisterRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class UserFactory {
    // TODO find a solution to use the global password encoder declared in SecurityConfig
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private UserFactory() {
        // Prevent instantiation
    }

    public static User createUser(RegisterRequest request) {
        if (request == null || request.role() == null) {
            return null;
        }
        return switch (request.role()) {
            case ROLE_ADMIN -> createAdminUser(request);
            case ROLE_USER -> createRegularUser(request);
            case ROLE_NUTRITIONIST -> createNutritionistUser(request);
        };
    }

    private static User createNutritionistUser(RegisterRequest request) {
        final Email email = new Email(request.username());
        final User user = new Nutritionist(PASSWORD_ENCODER.encode(request.password()), email);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUpdatedAt(LocalDateTime.now());
        final Authority authority = new Authority(request.role());
        authority.setUser(user);
        user.getAuthorities().add(authority);
        return user;
    }

    private static User createRegularUser(RegisterRequest request) {
        final Email email = new Email(request.username());
        final User user = new RegularUser(PASSWORD_ENCODER.encode(request.password()), email);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUpdatedAt(LocalDateTime.now());
        final Authority authority = new Authority(request.role());
        authority.setUser(user);
        user.getAuthorities().add(authority);
        return user;
    }

    private static User createAdminUser(RegisterRequest request) {
        final Email email = new Email(request.username());
        final User user = new Admin(PASSWORD_ENCODER.encode(request.password()), email);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUpdatedAt(LocalDateTime.now());
        final Authority authority = new Authority(request.role());
        authority.setUser(user);
        user.getAuthorities().add(authority);
        return user;
    }
}
