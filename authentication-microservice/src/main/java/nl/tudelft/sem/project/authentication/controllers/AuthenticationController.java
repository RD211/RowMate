package nl.tudelft.sem.project.authentication.controllers;

import nl.tudelft.sem.project.authentication.authentication.JwtTokenGenerator;
import nl.tudelft.sem.project.authentication.authentication.JwtUserDetailsService;
import nl.tudelft.sem.project.authentication.domain.user.RegistrationService;
import nl.tudelft.sem.project.authentication.AppUserModel;
import nl.tudelft.sem.project.authentication.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/")
public class AuthenticationController {

    private final transient AuthenticationManager authenticationManager;

    private final transient JwtTokenGenerator jwtTokenGenerator;

    private final transient JwtUserDetailsService jwtUserDetailsService;

    private final transient RegistrationService registrationService;

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
                                    RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.registrationService = registrationService;
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
    public ResponseEntity register(@Valid @RequestBody AppUserModel appUserModel) throws Exception {
        registrationService.registerUser(appUserModel.getUsername(), appUserModel.getPassword());
        return ResponseEntity.ok().build();
    }
}
