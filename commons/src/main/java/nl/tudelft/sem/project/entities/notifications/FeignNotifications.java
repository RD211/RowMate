package nl.tudelft.sem.project.entities.notifications;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8086", name = "notificationsController")
public interface FeignNotifications {
    @PostMapping("/sendNotification")
    ResponseEntity<String> sendNotification(@RequestBody NotificationDTO notificationDTO);
}
