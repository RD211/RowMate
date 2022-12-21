package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.authentication.AppUserModel;
import nl.tudelft.sem.project.authentication.AuthClient;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.authentication.Token;
import nl.tudelft.sem.project.gateway.AuthenticateUserModel;
import nl.tudelft.sem.project.gateway.CreateUserModel;
import nl.tudelft.sem.project.notifications.NotificationsClient;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ActiveProfiles({"test"})
class AuthenticationControllerTest {

    @Mock
    private transient AuthClient authClient;

    @Mock
    private transient UsersClient usersClient;

    @Mock
    private transient NotificationsClient notificationsClient;

    @InjectMocks
    AuthenticationController authenticationController;

    UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder().email("tester@testing.test")
                        .username("tester_master").build();
        when(usersClient.addUser(userDTO)).thenReturn(userDTO);
    }

    @Test
    void register() {
        when(authClient.authenticate(
                AppUserModel.builder()
                        .username(new Username("tester_master"))
                        .password(new Password("tester_god")).build())
        ).thenReturn(new Token("best_token"));
        var token = authenticationController.register(
                CreateUserModel.builder().email("tester@testing.test")
                        .password("tester_god")
                        .username("tester_master").build()
        );
        assertEquals("best_token", token.getBody());
    }

    @Test
    void authenticate() {
        when(authClient.authenticate(
                AppUserModel.builder()
                        .username(new Username("tester_master"))
                        .password(new Password("tester_god")).build())
        ).thenReturn(new Token("best_token"));
        var token = authenticationController.authenticate(
                AuthenticateUserModel.builder()
                        .password("tester_god")
                        .username("tester_master").build()
        );
        assertEquals("best_token", token.getBody());
    }
}