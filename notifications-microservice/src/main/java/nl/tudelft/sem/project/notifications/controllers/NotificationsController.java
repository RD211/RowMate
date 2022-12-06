package nl.tudelft.sem.project.notifications.controllers;

import nl.tudelft.sem.project.entities.notifications.NotificationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class NotificationsController {

    /**
     * The default test endpoint for the Notifications microservice.
     *
     * @return String
     */
    @GetMapping("/notifications")
    public ResponseEntity<String> notificationDefault() {
        return ResponseEntity.ok("[ENDPOINT] Notifications Microservice - WIP");
    }

    //TODO The endpoint should accept user, type of activity, and model w/ details relevant for model
    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@RequestParam NotificationDTO notificationDTO) {

        return ResponseEntity.ok("Notification sent to <None>.");
    }
}
