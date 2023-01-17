package nl.tudelft.sem.project.notifications.services;

import lombok.Getter;
import lombok.Setter;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.NotificationDTO;
import nl.tudelft.sem.project.notifications.mailTemplates.*;
import nl.tudelft.sem.project.notifications.exceptions.MailNotSentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static nl.tudelft.sem.project.notifications.Util.formatActivityDetailsMessage;

@Component
@Getter
public class NotificationsServiceImpl implements NotificationsService {

    @Autowired
    private transient JavaMailSender mailSender;

    @Autowired
    private transient HashMap<EventType, MailTemplate> messageTemplates;

    /**
     * Sends a mail notification to the email address
     * specified in the NotificationDTO, and customizes
     * the message depending on whether the event is
     * activity or user-related.
     *
     * @param notificationDTO the DTO containing details
     *                        about the user and the
     *                        activity, if applicable
     * @throws MailNotSentException if any issues are encountered regarding the sending of the notification
     */
    public SimpleMailMessage sendNotification(NotificationDTO notificationDTO) throws MailNotSentException {
        SimpleMailMessage message = new SimpleMailMessage();
        addMessageDetails(notificationDTO, message);
        message.setText(messageTemplates.get(notificationDTO.getEventType()).getMessage()
                + formatActivityDetailsMessage(notificationDTO));
        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new MailNotSentException(e.getMessage());
        }
        return message;
    }

    private void addMessageDetails(NotificationDTO notificationDTO, SimpleMailMessage message) {
        message.setFrom(Objects.requireNonNull(((JavaMailSenderImpl) mailSender).getUsername()));
        message.setTo(notificationDTO.getUserDTO().getEmail());
        message.setSubject(messageTemplates.get(notificationDTO.getEventType()).getSubject());
    }
}
