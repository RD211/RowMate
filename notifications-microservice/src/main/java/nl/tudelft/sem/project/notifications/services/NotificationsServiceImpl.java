package nl.tudelft.sem.project.notifications.services;

import nl.tudelft.sem.project.entities.notifications.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class NotificationsServiceImpl implements NotificationsService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendNotification(NotificationDTO notificationDTO) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("email");
        message.setTo(notificationDTO.getUserDTO().getEmail());
        message.setSubject("Subject");
        message.setText("Test email.");
        mailSender.send(message);
    }
}
