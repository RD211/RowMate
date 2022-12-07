package nl.tudelft.sem.project.notifications.config;

import nl.tudelft.sem.project.notifications.services.NotificationsService;
import nl.tudelft.sem.project.notifications.services.NotificationsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationsServiceBean {

    @Bean
    public NotificationsService getNotificationsService() {
        NotificationsService notificationsService = new NotificationsServiceImpl();

        return notificationsService;
    }
}
