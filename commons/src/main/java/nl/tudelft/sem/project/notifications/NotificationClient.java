package nl.tudelft.sem.project.notifications;

import feign.Headers;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8086", name = "notificationClient")
public interface NotificationClient {
    @PostMapping("/sendNotification")
    @Headers("Content-Type: application/json")
    String sendNotification(NotificationDTO notificationDTO);
}
