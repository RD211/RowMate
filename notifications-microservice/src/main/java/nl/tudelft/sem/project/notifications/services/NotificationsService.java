package nl.tudelft.sem.project.notifications.services;

import nl.tudelft.sem.project.notifications.NotificationDTO;

public interface NotificationsService {

    void sendNotification(NotificationDTO notificationDTO) throws Exception;
}
