package com.flavormetrics.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.flavormetrics.api.entity.User;
import com.flavormetrics.api.exception.impl.DuplicateEmailException;
import com.flavormetrics.api.exception.impl.NotAllowedRequestException;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.UserDto;
import com.flavormetrics.api.model.request.RegisterRequest;
import com.flavormetrics.api.repository.UserRepository;
import com.flavormetrics.api.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    private UserService userService;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USER_EMAIL = "user@email.com";
    private static final String USER_FIRST_NAME = "userFirstName";
    private static final String USER_LAST_NAME = "userLastName";

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testIfUserIsRegistered() {

        RegisterRequest data = new RegisterRequest(USER_EMAIL, USER_FIRST_NAME, USER_LAST_NAME);
        User savedUserInDb = new User(USER_EMAIL, USER_FIRST_NAME, USER_LAST_NAME);
        savedUserInDb.setId(USER_ID);

        Mockito.when(userRepository.existsByEmail(USER_EMAIL))
                .thenReturn(false);

        Mockito.when(authentication.isAuthenticated())
                .thenReturn(false);

        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(savedUserInDb);

        Data<UserDto> registeredUserResponse = userService.registerUser(data, authentication);

        assertEquals(USER_ID, registeredUserResponse.data().id());
        assertEquals(USER_EMAIL, registeredUserResponse.data().email());
        assertEquals(USER_FIRST_NAME, registeredUserResponse.data().firstName());
        assertEquals(USER_LAST_NAME, registeredUserResponse.data().lastName());
        assertNotNull(registeredUserResponse.data().id());
        assertNotNull(registeredUserResponse);
    }

    @Test
    void testIfThrows_DuplicateEmailException() {

        RegisterRequest data = new RegisterRequest(USER_EMAIL, USER_FIRST_NAME, USER_LAST_NAME);

        Mockito.when(userRepository.existsByEmail(USER_EMAIL))
        .thenReturn(true);
        
        assertThrows(DuplicateEmailException.class, () -> userService.registerUser(data, authentication));
    }

    @Test
    void testIfThrows_NotAllowedRequestException() {

        RegisterRequest data = new RegisterRequest(USER_EMAIL, USER_FIRST_NAME, USER_LAST_NAME);

        Mockito.when(authentication.isAuthenticated())
        .thenReturn(true);
        
        assertThrows(NotAllowedRequestException.class, () -> userService.registerUser(data, authentication));
    }

}
