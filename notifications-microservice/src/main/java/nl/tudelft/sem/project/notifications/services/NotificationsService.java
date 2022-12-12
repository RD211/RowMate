package nl.tudelft.sem.project.notifications.services;

import nl.tudelft.sem.project.entities.notifications.NotificationDTO;

public interface NotificationsService {

    void sendNotification(NotificationDTO notificationDTO) throws Exception;
}
