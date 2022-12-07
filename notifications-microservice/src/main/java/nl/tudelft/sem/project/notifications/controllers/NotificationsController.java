package nl.tudelft.sem.project.notifications.controllers;

import nl.tudelft.sem.project.entities.notifications.NotificationDTO;
import nl.tudelft.sem.project.notifications.services.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class NotificationsController {

    @Autowired
    private NotificationsService notificationsService;

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
        try {
            notificationsService.sendNotification(notificationDTO);
            return ResponseEntity.ok("Notification sent to " + notificationDTO.getUserDTO().getUsername() + ".");
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("The request could not be handled");
        }
    }
}
