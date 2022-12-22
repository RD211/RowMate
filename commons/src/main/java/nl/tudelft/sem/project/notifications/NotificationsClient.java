package nl.tudelft.sem.project.notifications;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "http://localhost:8086/api/notifications", name = "notificationsClient")
public interface NotificationsClient {

    @PostMapping("/sendNotification")
    @Headers("Content-Type: application/json")
    String sendNotification(NotificationDTO notificationDTO);
}
