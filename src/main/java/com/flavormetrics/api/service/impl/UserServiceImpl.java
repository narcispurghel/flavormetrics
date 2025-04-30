package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.entity.user.impl.RegularUser;
import com.flavormetrics.api.exception.impl.DuplicateEmailException;
import com.flavormetrics.api.exception.impl.NotAllowedRequestException;
import com.flavormetrics.api.factory.UserFactory;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.UserDto;
import com.flavormetrics.api.model.enums.RoleType;
import com.flavormetrics.api.model.request.LoginRequest;
import com.flavormetrics.api.model.request.RegisterRequest;
import com.flavormetrics.api.model.response.LoginResponse;
import com.flavormetrics.api.repository.NutritionistRepository;
import com.flavormetrics.api.repository.RegularUserRepository;
import com.flavormetrics.api.service.JWTService;
import com.flavormetrics.api.service.UserService;
import com.flavormetrics.api.util.ModelMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final UserFactory userFactory;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final RegularUserRepository regularUserRepository;
    private final NutritionistRepository nutritionistRepository;

    public UserServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JWTService jwtService,
            UserFactory userFactory,
            RegularUserRepository regularUserRepository,
            NutritionistRepository nutritionistRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userFactory = userFactory;
        this.regularUserRepository = regularUserRepository;
        this.nutritionistRepository = nutritionistRepository;
    }

    @Override
    @Transactional
    public Data<UserDto> registerUser(RegisterRequest data, Authentication authentication) {
        if (data.role() == RoleType.ROLE_ADMIN) {
            throw new NotAllowedRequestException(
                    "Bad request",
                    "Cannot perform register action because you do not have authorization to create admin users",
                    HttpStatus.BAD_REQUEST,
                    "data.role");
        }
        boolean isEmailUsed = existsByEmail(data.email(), data.role());
        if (isEmailUsed) {
            throw new DuplicateEmailException(
                    "Invalid email",
                    "This email address is not available",
                    HttpStatus.CONFLICT,
                    "data.email");
        }
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        if (isAuthenticated) {
            throw new NotAllowedRequestException(
                    "Bad request",
                    "Cannot perform register action because user is already authenticated",
                    HttpStatus.BAD_REQUEST,
                    "");
        }
        var user = userFactory.getUser(
                data.role(),
                data.email(),
                data.firstName(),
                data.lastName(),
                data.password()
        );
        user = switch (user) {
            case RegularUser u -> regularUserRepository.save(u);
            case Nutritionist n -> nutritionistRepository.save(n);
            default -> null;
        };
        logger.info("User created: {}", user);
        return Data.body(ModelMapper.toUserDto(user));
    }

    @Override
    @Transactional
    public LoginResponse authenticate(LoginRequest data, Authentication authentication) {
        boolean isAuthenticated = authentication.isAuthenticated();
        if (isAuthenticated) {
            throw new NotAllowedRequestException(
                    "Invalid request",
                    "Cannot perform register action because user is already authenticated",
                    HttpStatus.BAD_REQUEST,
                    "");
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                data.email(),
                data.password()
        );
        Authentication authenticationWithUser = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationWithUser);
        UserDetails user = userDetailsService.loadUserByUsername(data.email());
        String jwtToken = jwtService.generateToken(user);
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new LoginResponse(user.getUsername(), roles, jwtToken);
    }

    @Override
    public boolean existsByEmail(String email, RoleType role) {
        return switch (role) {
            case ROLE_USER -> regularUserRepository.existsByUsername_Value(email);
            case ROLE_NUTRITIONIST -> false;
            case ROLE_ADMIN -> false;
        };
    }
}
