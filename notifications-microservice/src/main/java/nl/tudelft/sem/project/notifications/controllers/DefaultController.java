package nl.tudelft.sem.project.notifications.controllers;

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
public class DefaultController {
    /**
     * The default test endpoint for the Notifications microservice.
     *
     * @return String
     */
    @GetMapping("/notifications")
    public ResponseEntity<String> notificationDefault() {
        return ResponseEntity.ok("[ENDPOINT] Notifications Microservice - WIP");
    }
}
