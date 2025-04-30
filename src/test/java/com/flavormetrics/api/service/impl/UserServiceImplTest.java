package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.factory.UserFactory;
import com.flavormetrics.api.model.enums.RoleType;
import com.flavormetrics.api.repository.RegularUserRepository;
import com.flavormetrics.api.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private RegularUserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private UserFactory userFactory;

    private UserService userService;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USER_EMAIL = "user@email.com";
    private static final String USER_FIRST_NAME = "userFirstName";
    private static final String USER_LAST_NAME = "userLastName";
    private static final String USER_PASSWORD = "userPassword";
    private static final RoleType USER_ROLE = RoleType.ROLE_USER;

    //TODO actualize tests
/*    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testIfUserIsRegistered() {

        RegisterRequest data = new RegisterRequest(USER_EMAIL, USER_FIRST_NAME, USER_LAST_NAME,
                USER_PASSWORD, USER_ROLE);

        Mockito.when(userFactory.getUser(USER_ROLE, USER_FIRST_NAME, USER_LAST_NAME,
                        USER_PASSWORD, USER_EMAIL))
                .thenReturn(null);

        var savedUserInDb = new RegularUser.Builder()
                .id(USER_ID)
                .build();

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
    void testIfThrowsDuplicateEmailException() {

        RegisterRequest data = new RegisterRequest(USER_EMAIL, USER_FIRST_NAME, USER_LAST_NAME);

        Mockito.when(userRepository.existsByEmail(USER_EMAIL))
                .thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.registerUser(data, authentication));
    }

    @Test
    void testIfThrowsNotAllowedRequestException() {

        RegisterRequest data = new RegisterRequest(USER_EMAIL, USER_FIRST_NAME, USER_LAST_NAME);

        Mockito.when(authentication.isAuthenticated())
                .thenReturn(true);

        assertThrows(NotAllowedRequestException.class, () -> userService.registerUser(data, authentication));
    }*/

}
