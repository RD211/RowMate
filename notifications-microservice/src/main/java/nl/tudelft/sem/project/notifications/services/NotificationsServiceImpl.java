package nl.tudelft.sem.project.notifications.services;

import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.NotificationDTO;
import nl.tudelft.sem.project.notifications.mailTemplates.*;
import nl.tudelft.sem.project.notifications.exceptions.MailNotSentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.HashMap;

public class NotificationsServiceImpl implements NotificationsService {

    @Autowired
    private transient JavaMailSender mailSender;

    @Autowired
    private transient HashMap<EventType, MailTemplate> messageTemplates;

    /* TODO Finish method (properly adding activity details) once
    ActivityDTO has been completely implemented. */
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
        String activityDetails;

        message.setFrom("noreply.rowing.delft@gmail.com");
        message.setTo(notificationDTO.getUserDTO().getEmail());
        message.setSubject(messageTemplates.get(notificationDTO.getEventType()).getSubject());

        EventType eventType = notificationDTO.getEventType();

        if (eventType == EventType.SIGN_UP
            || eventType == EventType.RESET_PASSWORD
            || eventType == EventType.TEST) {
            activityDetails = "\nActivity details:\n"
                    + notificationDTO.getActivityDTO().toString();
            message.setText(messageTemplates.get(notificationDTO.getEventType()).getMessage()
                    + activityDetails);
        } else {
            message.setText(messageTemplates.get(notificationDTO.getEventType()).getMessage());
        }
        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new MailNotSentException(e.getMessage());
        }
    }
}
