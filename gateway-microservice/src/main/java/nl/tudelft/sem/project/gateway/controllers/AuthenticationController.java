package nl.tudelft.sem.project.gateway.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import nl.tudelft.sem.project.authentication.AppUserModel;
import nl.tudelft.sem.project.authentication.AuthClient;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.authentication.ResetPasswordModel;
import nl.tudelft.sem.project.gateway.AuthenticateUserModel;
import nl.tudelft.sem.project.gateway.CreateUserModel;
import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.NotificationsClient;
import nl.tudelft.sem.project.notifications.NotificationDTO;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    @Autowired
    private transient UsersClient usersClient;

    @Autowired
    private transient AuthClient authClient;

    @Autowired
    private transient NotificationsClient notificationsClient;

    /**
     * The register endpoint.
     * This endpoint registers a user by performing two different API calls
     * to the authentication microservice and the user one.
     *
     * @param createUserModel the model that contains username, email and password.
     * @return a token that the user can directly use to authenticate in the future.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid CreateUserModel createUserModel) {
        var appUserModel = AppUserModel.builder()
                .username(new Username(createUserModel.getUsername()))
                .password(new Password(createUserModel.getPassword())).build();
        authClient.register(appUserModel);

        var userDTO = UserDTO.builder().username(createUserModel.getUsername())
                        .email(createUserModel.getEmail()).build();

        usersClient.addUser(userDTO);

        // Send notification that the user has registered using this email.
        try {
            new Thread(() -> notificationsClient.sendNotification(NotificationDTO.builder()
                    .userDTO(userDTO)
                    .eventType(EventType.SIGN_UP)
                    .build())).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        var token = authClient.authenticate(appUserModel);

        return ResponseEntity.ok(token.getToken());
    }

    /**
     * The authentication endpoint.
     * Calls the authentication microservice directly.
     *
     * @param authenticateUserModel the model with the username and password.
     * @return a token that can be used for further authentication.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody @Valid AuthenticateUserModel authenticateUserModel) {
        var appUserModel = AppUserModel.builder()
                .username(new Username(authenticateUserModel.getUsername()))
                .password(new Password(authenticateUserModel.getPassword())).build();
        var token = authClient.authenticate(appUserModel);

        return ResponseEntity.ok(token.getToken());
    }

    /**
     * The reset password with previous endpoint.
     * Resets the password of a user given the previous one.
     *
     * @param resetPasswordModel the model that contains username, previous password and new password.
     * @return nothing.
     */
    @PostMapping("/reset_password_with_previous")
    public ResponseEntity<Void> resetPasswordWithPrevious(
            @RequestBody @Valid ResetPasswordModel resetPasswordModel) {
        authClient.resetPasswordWithPrevious(resetPasswordModel);

        // Send notification that password has been reset using previous password.
        try {
            new Thread(() -> {
                UserDTO userDTO = usersClient.getUserByUsername(resetPasswordModel.getAppUserModel().getUsername());
                notificationsClient.sendNotification(NotificationDTO.builder()
                    .userDTO(userDTO)
                    .eventType(EventType.RESET_PASSWORD_CONFIRM)
                    .build());
            }).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Reset password by email.
     * This endpoint will email the user with a reset password link.
     *
     * @param username the username of the user.
     * @return nothing.
     */
    @PostMapping("/reset_password_with_email")
    public ResponseEntity<Void> resetPasswordWithEmail(@RequestBody @Valid Username username) {
        var token = authClient.getEmailResetPasswordToken(username);

        // Send password reset link to email address.
        try {
            new Thread(() -> {
                UserDTO userDTO = usersClient.getUserByUsername(username);
                String tokenLink = "http://localhost:8087/api/authentication/reset_password?token=" + token;
                notificationsClient.sendNotification(NotificationDTO.builder()
                    .userDTO(userDTO)
                    .eventType(EventType.RESET_PASSWORD)
                    .optionalField(tokenLink)
                    .build());
            }).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return ResponseEntity.ok().build();
    }

    /**
     * Reset password from email token.
     * This endpoint will reset the password of a user when they use the
     * link they were sent via email.
     *
     * @param resetToken the token.
     * @param newPassword the new password.
     * @return nothing.
     */
    @PostMapping("/reset_password")
    public ResponseEntity<Void> resetPassword(@Valid @NotNull @RequestParam("token") String resetToken,
                                              @Valid @NotNull @RequestBody String newPassword) {
        authClient.emailResetPassword(
                resetToken,
                new Password(newPassword)
        );
        return ResponseEntity.ok().build();
    }
}
