package nl.tudelft.sem.project.authentication.controllers;

import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.authentication.ResetPasswordModel;
import nl.tudelft.sem.project.authentication.authentication.JwtTokenGenerator;
import nl.tudelft.sem.project.authentication.authentication.JwtUserDetailsService;
import nl.tudelft.sem.project.authentication.domain.user.ChangePasswordService;
import nl.tudelft.sem.project.authentication.domain.user.RegistrationService;
import nl.tudelft.sem.project.authentication.AppUserModel;
import nl.tudelft.sem.project.authentication.Token;
import nl.tudelft.sem.project.shared.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URL;

@RestController
@RequestMapping(value = "api/")
public class AuthenticationController {

    private final transient AuthenticationManager authenticationManager;

    private final transient JwtTokenGenerator jwtTokenGenerator;

    private final transient JwtUserDetailsService jwtUserDetailsService;

    private final transient RegistrationService registrationService;

    private final transient ChangePasswordService changePasswordService;

    /**
     * Instantiates a new UsersController.
     *
     * @param authenticationManager the authentication manager
     * @param jwtTokenGenerator     the token generator
     * @param jwtUserDetailsService the user service
     * @param registrationService   the registration service
     */
    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenGenerator jwtTokenGenerator,
                                    JwtUserDetailsService jwtUserDetailsService,
                                    RegistrationService registrationService,
                                    ChangePasswordService changePasswordService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.registrationService = registrationService;
        this.changePasswordService = changePasswordService;
    }

    /**
     * Authenticates a user given his credentials.
     *
     * @param request the app user model that contains the username and password.
     * @return a token which the user can use in the future.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Token> authenticate(@Valid @RequestBody AppUserModel request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername().getName(),
                        request.getPassword().getPasswordValue()));
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(request.getUsername().getName());
        final Token jwtToken = jwtTokenGenerator.generateToken(userDetails);
        return ResponseEntity.ok(jwtToken);
    }

    /**
     * Register endpoint. Registers a user given the username and password.
     *
     * @param appUserModel the user model that contains the user and password.
     * @return nothing
     * @throws Exception if the registration was unsuccessful.
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AppUserModel appUserModel) throws Exception {
        registrationService.registerUser(appUserModel.getUsername(), appUserModel.getPassword(), false);
        return ResponseEntity.ok().build();
    }


    /**
     * Resets password given the previous one.
     *
     * @param resetPasswordModel the model that contains username, prev password and the new one.
     * @return nothing.
     * @throws Exception if password is wrong or user does not exist.
     */
    @PostMapping("/reset_password_with_previous")
    public ResponseEntity<Void> resetPasswordWithPrevious(
            @Valid @NotNull @RequestBody ResetPasswordModel resetPasswordModel) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        resetPasswordModel.getAppUserModel().getUsername().getName(),
                        resetPasswordModel.getAppUserModel().getPassword().getPasswordValue()));
        changePasswordService.changePassword(resetPasswordModel.getAppUserModel().getUsername(),
                resetPasswordModel.getNewPassword());
        return ResponseEntity.ok().build();
    }

    /**
     * Gets email reset password token.
     * This will return a token that can be used to reset the password via email.
     * This token should be redirected to the reset password email.
     *
     * @param username the username of the user to reset.
     * @return the token.
     * @throws Exception if the username does not exist.
     */
    @PostMapping("/get_email_reset_password_token")
    public ResponseEntity<String> getEmailResetPasswordToken(@Valid @NotNull @RequestBody Username username)
            throws Exception {
        var token = jwtTokenGenerator.generateTokenForResetPassword(username);
        return ResponseEntity.ok(token.getToken());
    }

    /**
     * Reset password via email endpoint.
     * Wil reset password given the special token that contains the username.
     *
     * @param resetToken the token with username.
     * @param newPassword the new password.
     * @return nothing.
     * @throws Exception if the user is not found or the token is invalid.
     */
    @PostMapping("/reset_password_email")
    public ResponseEntity<Void> emailResetPassword(@Valid @NotNull @RequestParam("token") String resetToken,
                                             @Valid @NotNull @RequestBody Password newPassword) throws Exception {
        var username = changePasswordService.getUsernameFromToken(resetToken);
        changePasswordService.changePassword(username, newPassword);
        return ResponseEntity.ok().build();
    }
}
