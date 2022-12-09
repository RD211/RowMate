package nl.tudelft.sem.project.notifications.services;

import nl.tudelft.sem.project.entities.notifications.EventType;
import nl.tudelft.sem.project.entities.notifications.NotificationDTO;
import nl.tudelft.sem.project.entities.notifications.mailTemplates.*;
import nl.tudelft.sem.project.notifications.config.JavaMailSenderConfig;
import nl.tudelft.sem.project.notifications.exceptions.MailNotSentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.File;
import java.util.HashMap;

public class NotificationsServiceImpl implements NotificationsService {

    @Autowired
    private transient JavaMailSender mailSender;

    private transient HashMap<EventType, MailTemplate> messageTemplates;

    /**
     * Constructor method. Puts the message templates
     * in the messageTemplates table.
     */
    public NotificationsServiceImpl() {
        messageTemplates = new HashMap<>() {{
                put(EventType.ACTIVITY_FULL, new MailActivityFull());
                put(EventType.ACTIVITY_START_SOON, new MailActivityStartSoon());
                put(EventType.RESET_PASSWORD, new MailResetPassword());
                put(EventType.SIGN_UP, new MailSignUp());
                put(EventType.TEST, new MailTest());
                put(EventType.USER_JOINED, new MailUserJoined());
                put(EventType.USER_LEFT, new MailUserLeft());
            }};
    }

    /**
     * Sends a mail notification to the email address
     * specified in the NotificationDTO, and customizes
     * the message depending on whether the event is
     * activity or user-related.
     *
     * @param notificationDTO the DTO containing details
     *                        about the user and the
     *                        activity, if applicable
     * @throws Exception if any issues are encountered regarding the sending of the notification
     */
    public void sendNotification(NotificationDTO notificationDTO) throws MailNotSentException {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("test@localhost");
        message.setTo(notificationDTO.getUserDTO().getEmail());
        message.setSubject(messageTemplates.get(notificationDTO.getEventType()).getSubject());

        EventType eventType = notificationDTO.getEventType();

        if (eventType == EventType.SIGN_UP
            || eventType == EventType.RESET_PASSWORD
            || eventType == EventType.TEST) {
            sendNotificationUserRelated(notificationDTO, message);
        } else {
            sendNotificationActivityRelated(notificationDTO, message);
        }
    }

    //TODO finish method once Activity DTO has been properly implemented
    /**
     * Send activity-related notification, including
     * details about the activity.
     */
    private void sendNotificationActivityRelated(NotificationDTO notificationDTO, SimpleMailMessage message) {
        String activityDetails = "\nActivity details:\n"
                + notificationDTO.getActivityDTO().toString();

        message.setText(messageTemplates.get(notificationDTO.getEventType()).getMessage()
                + activityDetails);
        mailSender.send(message);
    }

    /**
     * Send notification without any activity details.
     */
    private void sendNotificationUserRelated(NotificationDTO notificationDTO, SimpleMailMessage message) {
        message.setText(messageTemplates.get(notificationDTO.getEventType()).getMessage());
        mailSender.send(message);
    }
}
