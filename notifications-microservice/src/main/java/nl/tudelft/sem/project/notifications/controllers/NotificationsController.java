package nl.tudelft.sem.project.notifications.controllers;

import nl.tudelft.sem.project.entities.activities.TrainingDTO;
import nl.tudelft.sem.project.entities.notifications.EventType;
import nl.tudelft.sem.project.entities.notifications.NotificationDTO;
import nl.tudelft.sem.project.entities.users.UserDTO;
import nl.tudelft.sem.project.notifications.services.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.sql.SQLOutput;
import java.util.UUID;

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

    @Autowired
    private FeignController feignController;

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
     * @param notificationDTO the DTO containing user and
     *                        activity data
     * @return ResponseEntity indicating whether the
     * email has been sent successfully
     */
    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationDTO notificationDTO) {
        try {
            notificationsService.sendNotification(notificationDTO);
            return ResponseEntity.ok("Notification sent to " + notificationDTO.getUserDTO().getUsername() + ".");
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> sendNotifFeign(@RequestBody NotificationDTO notificationDTO) {
        return feignController.sendNotification(notificationDTO);
    }

    /**
     * Test endpoint.
     * Sends an email using only the specified email address, the
     * rest of the NotificationDTO is filled with placeholders.
     * The event type will be marked as "TEST".
     * @param email the address to send the email to
     * @return ResponseEntity indicating whether the mail has
     * been sent successfully
     */
    @PostMapping("/sendNotifManual")
    public ResponseEntity<String> sendNotification(@RequestBody String email) {
        try {
            NotificationDTO notificationDTO = NotificationDTO.builder()
                    .userDTO(UserDTO.builder()
                    .email(email)
                    .username("none")
                    .id(UUID.randomUUID()).build())
                    .eventType(EventType.TEST)
                    .activityDTO(new TrainingDTO())
                    .build();

            notificationsService.sendNotification(notificationDTO);
            return ResponseEntity.ok("Notification sent to " + notificationDTO.getUserDTO().getEmail() + ".");
        }
        catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
