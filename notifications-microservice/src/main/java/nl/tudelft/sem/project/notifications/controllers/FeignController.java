package nl.tudelft.sem.project.notifications.controllers;

import nl.tudelft.sem.project.entities.notifications.NotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8086", name = "notificationsController")
public interface FeignController {
    @PostMapping("/sendNotification")
    ResponseEntity<String> sendNotification(@RequestBody NotificationDTO notificationDTO);
}
