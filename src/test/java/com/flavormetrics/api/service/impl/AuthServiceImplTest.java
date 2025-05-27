package com.flavormetrics.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.flavormetrics.api.entity.Authority;
import com.flavormetrics.api.entity.Email;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.exception.impl.DuplicateEmailException;
import com.flavormetrics.api.exception.impl.NotAllowedRequestException;
import com.flavormetrics.api.factory.UserFactory;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.enums.RoleType;
import com.flavormetrics.api.model.request.LoginRequest;
import com.flavormetrics.api.model.request.RegisterRequest;
import com.flavormetrics.api.model.response.LoginResponse;
import com.flavormetrics.api.model.response.RegisterResponse;
import com.flavormetrics.api.repository.UserRepository;
import com.flavormetrics.api.service.AuthService;
import com.flavormetrics.api.service.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    private static final String USERNAME = "test@flavormetrics.com";
    private static final UUID ID = UUID.randomUUID();
    private static final String FIRST_NAME = "Test";
    private static final String LAST_NAME = "Nutritionist";
    private static final RoleType ROLE = RoleType.ROLE_NUTRITIONIST;
    private static final String PASSWORD = "testpassword";
    private static final String JWT = "mockJwt";

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFactory userFactory;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(
                authenticationManager,
                jwtService,
                userRepository,
                userFactory
        );
    }

    @Test
    void registerUser() {
        final Email email = new Email(USERNAME);
        final User user = new Nutritionist(PASSWORD, email);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setId(ID);
        user.setAuthorities(List.of(new Authority(ROLE)));
        final RegisterRequest request = new RegisterRequest(USERNAME, FIRST_NAME, LAST_NAME, PASSWORD, ROLE);
        Mockito.when(userRepository.existsByUsername_Value(USERNAME))
                .thenReturn(false);
        Mockito.when(userFactory.createUser(request))
                .thenReturn(user);
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        final Data<RegisterResponse> response = authService.registerUser(request, null);
        assertNotNull(response);
        assertNotNull(response.data());
        assertEquals(ID, response.data().userId());
        assertEquals(USERNAME, response.data().username());
        assertEquals(FIRST_NAME, response.data().firstName());
        assertEquals(LAST_NAME, response.data().lastName());
        assertEquals(ROLE, response.data().role());
    }

    @Test
    void registerUserThrowsNotAllowedRequestExceptionWithNullData() {
        assertThrows(NotAllowedRequestException.class, () -> authService.registerUser(null, null));
    }

    @Test
    void registerUserThrowsNotAllowedRequestExceptionWithAdminRole() {
        final RegisterRequest request = new RegisterRequest(USERNAME, FIRST_NAME, LAST_NAME, PASSWORD, RoleType.ROLE_ADMIN);
        assertThrows(NotAllowedRequestException.class, () -> authService.registerUser(request, null));
    }

    @Test
    void registerUserThrowsNotAllowedRequestExceptionWithNonNullAuthentication() {
        final Email email = new Email(USERNAME);
        final User user = new Nutritionist(PASSWORD, email);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setId(ID);
        user.setAuthorities(List.of(new Authority(ROLE)));
        final RegisterRequest request = new RegisterRequest(USERNAME, FIRST_NAME, LAST_NAME, PASSWORD, ROLE);
        Mockito.when(userRepository.existsByUsername_Value(USERNAME))
                .thenReturn(false);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        assertThrows(NotAllowedRequestException.class, () -> authService.registerUser(request, authentication));
    }

    @Test
    void registerUserThrowsDuplicateEmailException() {
        final RegisterRequest request = new RegisterRequest(USERNAME, FIRST_NAME, LAST_NAME, PASSWORD, ROLE);
        Mockito.when(userRepository.existsByUsername_Value(USERNAME))
                .thenReturn(true);
        assertThrows(DuplicateEmailException.class, () -> authService.registerUser(request, null));
    }

    @Test
    void authenticate() {
        final Email email = new Email(USERNAME);
        final User user = new Nutritionist(PASSWORD, email);
        user.setAuthorities(List.of(new Authority(ROLE)));
        final List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        final LoginRequest request = new LoginRequest(USERNAME, PASSWORD);
        Mockito.when(userRepository.getByUsername_Value(USERNAME))
                .thenReturn(user);
        Mockito.when(jwtService.generateToken(user))
                .thenReturn(JWT);
        final LoginResponse response = authService.authenticate(request, null);
        assertNotNull(response);
        assertEquals(USERNAME, response.username());
        assertEquals(roles, response.roles());
        assertEquals(JWT, response.token());
    }

    @Test
    void authenticateThrowsNotAllowedRequestExceptionWithNonNullAuthentication() {
        final Email email = new Email(USERNAME);
        final User user = new Nutritionist(PASSWORD, email);
        user.setAuthorities(List.of(new Authority(ROLE)));
        final LoginRequest request = new LoginRequest(USERNAME, PASSWORD);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        assertThrows(NotAllowedRequestException.class, () -> authService.authenticate(request, authentication));
    }
}