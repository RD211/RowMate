package nl.tudelft.sem.project.authentication.controllers;

import nl.tudelft.sem.project.authentication.AppUserModel;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.authentication.ResetPasswordModel;
import nl.tudelft.sem.project.authentication.Token;
import nl.tudelft.sem.project.authentication.authentication.JwtTokenGenerator;
import nl.tudelft.sem.project.authentication.authentication.JwtUserDetailsService;
import nl.tudelft.sem.project.authentication.domain.user.ChangePasswordService;
import nl.tudelft.sem.project.authentication.domain.user.RegistrationService;
import nl.tudelft.sem.project.shared.Username;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationControllerTest {

    @Mock
    private transient AuthenticationManager authenticationManager;

    @Mock
    private transient JwtTokenGenerator jwtTokenGenerator;

    @Mock
    private transient JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private transient RegistrationService registrationService;

    @Mock
    private transient ChangePasswordService changePasswordService;

    @InjectMocks
    AuthenticationController authenticationController;

    @Test
    void authenticate() {
        var username = new Username("tester");
        var password = new Password("best_pass");

        var appUserModel = new AppUserModel(username, password);

        var user = new User(username.getName(), password.getPasswordValue(),
                List.of());
        var token = new Token("safd");
        when(jwtUserDetailsService.loadUserByUsername(
                username.getName()
        )).thenReturn(user);
        when(jwtTokenGenerator.generateToken(user)).thenReturn(token);

        var result = authenticationController.authenticate(appUserModel).getBody();
        assertEquals(token, result);
    }

    @Test
    void register() throws Exception {
        var username = new Username("tester");
        var password = new Password("best_pass");

        var appUserModel = new AppUserModel(username, password);
        assertDoesNotThrow(() -> authenticationController.register(appUserModel));
        verify(registrationService, times(1)).registerUser(
                appUserModel.getUsername(), appUserModel.getPassword(), false
        );
    }

    @Test
    void resetPasswordWithPrevious() throws Exception {
        var username = new Username("tester");
        var password = new Password("best_pass");
        var newPassword = new Password("other_pass");

        var appUserModel = new AppUserModel(username, password);
        var resetPasswordModel = new ResetPasswordModel(appUserModel, newPassword);

        assertDoesNotThrow(() -> authenticationController.resetPasswordWithPrevious(resetPasswordModel));
        verify(changePasswordService, times(1)).changePassword(
                resetPasswordModel.getAppUserModel().getUsername(),
                resetPasswordModel.getNewPassword()
        );
        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(
                        resetPasswordModel.getAppUserModel().getUsername().getName(),
                        resetPasswordModel.getAppUserModel().getPassword().getPasswordValue())
        );
    }

    @Test
    void getEmailResetPasswordToken() throws Exception {
        var username = new Username("test");
        var token = new Token("asdasd");
        when(jwtTokenGenerator.generateTokenForResetPassword(username)).thenReturn(token);
        var response = authenticationController.getEmailResetPasswordToken(username).getBody();
        assertEquals(token.getToken(), response);
    }

    @Test
    void emailResetPassword() throws Exception {
        var username = new Username("tester");
        var token = new Token("testToken");
        var pass = new Password("new_pass");
        when(changePasswordService.getUsernameFromToken(token.getToken())).thenReturn(
                username
        );
        assertDoesNotThrow(() -> authenticationController.emailResetPassword(token.getToken(), pass));
        verify(changePasswordService, times(1)).changePassword(username, pass);
    }
}