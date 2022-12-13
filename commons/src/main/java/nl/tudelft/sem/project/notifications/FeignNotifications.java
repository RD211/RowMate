package nl.tudelft.sem.project.notifications;

import feign.Headers;
import feign.RequestLine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface FeignNotifications {
    @RequestLine("POST /sendNotification")
    @Headers("Content-Type: application/json")
    ResponseEntity<String> sendNotification(@RequestBody NotificationDTO notificationDTO);
}
