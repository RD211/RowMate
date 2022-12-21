package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.authentication.*;
import nl.tudelft.sem.project.gateway.AuthenticateUserModel;
import nl.tudelft.sem.project.gateway.CreateUserModel;
import nl.tudelft.sem.project.notifications.NotificationsClient;
import nl.tudelft.sem.project.notifications.NotificationDTO;
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
import static org.mockito.Mockito.*;

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
        when(usersClient.getUserByUsername(new Username("tester_master"))).thenReturn(userDTO);
    }

    @Test
    void registerTest() {
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
    void authenticateTest() {
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

    @Test
    void resetPasswordWithPreviousTest() {
        var resetPasswordModel =  new ResetPasswordModel(AppUserModel.builder()
                .username(new Username("tester_master"))
                .password(new Password("tester_god")).build(), new Password("AN AMAZING PASSWORD"));

        assertDoesNotThrow(() -> authenticationController.resetPasswordWithPrevious(resetPasswordModel));
        verify(authClient, times(1)).resetPasswordWithPrevious(resetPasswordModel);
        verifyNoMoreInteractions(authClient);
    }

    @Test
    void resetPasswordWithEmailTest() {
        var username = new Username("tester");

        when(authClient.getEmailResetPasswordToken(username)).thenReturn("a token");

        var userDTO = UserDTO.builder()
                .email("test@testing.com").username(username.getName()).build();
        when(usersClient.getUserByUsername(username)).thenReturn(userDTO);

        assertDoesNotThrow(() -> authenticationController.resetPasswordWithEmail(username));

        verify(notificationsClient, times(1)).sendNotification(any(NotificationDTO.class));
        verify(usersClient, times(1)).getUserByUsername(username);
        verify(authClient, times(1)).getEmailResetPasswordToken(username);
        verifyNoMoreInteractions(authClient, usersClient);
    }

    @Test
    void resetPasswordTest() {
        var newPassword = "a new password";
        var token = "a token";


        assertDoesNotThrow(() -> authenticationController.resetPassword(token, newPassword));

        verify(authClient, times(1)).emailResetPassword(token, new Password(newPassword));
        verifyNoMoreInteractions(authClient);
    }
}