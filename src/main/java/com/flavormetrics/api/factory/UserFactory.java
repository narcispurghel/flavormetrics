package com.flavormetrics.api.factory;

import com.flavormetrics.api.entity.Authority;
import com.flavormetrics.api.entity.Email;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.entity.user.impl.Admin;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.entity.user.impl.RegularUser;
import com.flavormetrics.api.model.request.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserFactory {
    private final PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(RegisterRequest request) {
        if (request == null || request.role() == null) {
            return null;
        }
        return switch (request.role()) {
            case ROLE_ADMIN -> createAdminUser(request);
            case ROLE_USER -> createRegularUser(request);
            case ROLE_NUTRITIONIST -> createNutritionistUser(request);
        };
    }

    private User createNutritionistUser(RegisterRequest request) {
        final Email email = new Email(request.username());
        final User user = new Nutritionist(passwordEncoder.encode(request.password()), email);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUpdatedAt(LocalDateTime.now());
        final Authority authority = new Authority(request.role());
        authority.setUser(user);
        user.getAuthorities().add(authority);
        return user;
    }

    private User createRegularUser(RegisterRequest request) {
        final Email email = new Email(request.username());
        final User user = new RegularUser(passwordEncoder.encode(request.password()), email);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUpdatedAt(LocalDateTime.now());
        final Authority authority = new Authority(request.role());
        authority.setUser(user);
        user.getAuthorities().add(authority);
        return user;
    }

    private User createAdminUser(RegisterRequest request) {
        final Email email = new Email(request.username());
        final User user = new Admin(passwordEncoder.encode(request.password()), email);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUpdatedAt(LocalDateTime.now());
        final Authority authority = new Authority(request.role());
        authority.setUser(user);
        user.getAuthorities().add(authority);
        return user;
    }
}
