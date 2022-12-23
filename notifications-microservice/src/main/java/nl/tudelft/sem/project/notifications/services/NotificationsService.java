package nl.tudelft.sem.project.notifications.services;

import nl.tudelft.sem.project.notifications.NotificationDTO;
import org.springframework.mail.SimpleMailMessage;

public interface NotificationsService {

    SimpleMailMessage sendNotification(NotificationDTO notificationDTO) throws Exception;
}
