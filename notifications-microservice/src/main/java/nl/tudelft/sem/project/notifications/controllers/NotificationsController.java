package nl.tudelft.sem.project.notifications.controllers;

import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.NotificationDTO;
import nl.tudelft.sem.project.notifications.services.NotificationsService;
import nl.tudelft.sem.project.users.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private transient NotificationsService notificationsService;

    /**
     * The default test endpoint for the Notifications microservice.
     *
     * @return String
     */
    @GetMapping("/notifications")
    public ResponseEntity<String> notificationDefault() {
        return ResponseEntity.ok("[ENDPOINT] Notifications Microservice - WIP");
    }

    /**
     * The endpoint to be used for sending notifications
     * about a specific activity through mail to a user.
     *
     * @param notificationDTO the DTO containing user and
     *                        activity data
     * @return ResponseEntity indicating whether the email has been sent successfully
     */
    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationDTO notificationDTO) {
        try {
            notificationsService.sendNotification(notificationDTO);
            return ResponseEntity.ok("Notification sent to " + notificationDTO.getUserDTO().getUsername() + ".");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Test endpoint.
     * Sends an email using only the specified email address, the
     * rest of the NotificationDTO is filled with placeholders.
     * The event type will be marked as "TEST".
     *
     * @param email the address to send the email to
     * @return ResponseEntity indicating whether the mail has been sent successfully
    */
    @PostMapping("/sendNotifManual")
    public ResponseEntity<String> sendNotification(@RequestBody String email) {
        try {
            NotificationDTO notificationDTO = NotificationDTO.builder()
                    .userDTO(UserDTO.builder()
                    .email(email)
                    .username("none").build())
                    .eventType(EventType.TEST)
                    .activityDTO(new TrainingDTO())
                    .build();

            notificationsService.sendNotification(notificationDTO);
            return ResponseEntity.ok("Notification sent to " + notificationDTO.getUserDTO().getEmail() + ".");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.toString());
        }
    }
}
