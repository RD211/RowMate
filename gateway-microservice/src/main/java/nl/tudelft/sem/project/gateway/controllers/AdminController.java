package nl.tudelft.sem.project.gateway.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserEmail;
import nl.tudelft.sem.project.users.UsersClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/admin")
public class AdminController {
    private final transient UsersClient usersClient;

    @Autowired
    public AdminController(UsersClient usersClient) {
        this.usersClient = usersClient;
    }

    /**
     * Deletes a user by username.
     * This is an admin endpoint.
     *
     * @param username the username of the user to delete
     * @return nothing
     */
    @DeleteMapping("/delete_user_by_username")
    public ResponseEntity deleteUserByUsername(@Valid @RequestBody @NotNull String username) {
        usersClient.deleteUserByUsername(
                new Username(username)
        );
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a user by email.
     * This is an admin endpoint.
     *
     * @param email the email of the user to delete
     * @return nothing
     */
    @DeleteMapping("/delete_user_by_email")
    public ResponseEntity deleteUserByEmail(@Valid @RequestBody @NotNull String email) {
        usersClient.deleteUserByEmail(
                new UserEmail(email)
        );
        return ResponseEntity.ok().build();
    }
}
