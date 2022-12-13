package nl.tudelft.sem.project.activities.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
@SecurityRequirement(name = "Bearer Authentication")
public class DefaultController {

    //private final transient AuthManager authManager;

    /**
     * Instantiates a new controller.
     *
     */
    @Autowired
    //public DefaultController(AuthManager authManager) {
    //    this.authManager = authManager;
    //}
    public DefaultController() {

    }


    /**
     * Gets example by id.
     *
     * @return the example found in the database with the given id
     */
    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello Gamer!");
    }
}
