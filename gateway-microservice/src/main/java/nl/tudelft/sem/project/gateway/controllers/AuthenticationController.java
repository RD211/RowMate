package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.authentication.AppUserModel;
import nl.tudelft.sem.project.authentication.AuthClient;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.gateway.AuthenticateUserModel;
import nl.tudelft.sem.project.gateway.CreateUserModel;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    @Autowired
    private transient UsersClient usersClient;

    @Autowired
    private transient AuthClient authClient;


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
}
