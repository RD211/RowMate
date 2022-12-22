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

@Component
@Getter
@Setter
public class NotificationsServiceImpl implements NotificationsService {

    @Autowired
    private transient JavaMailSender mailSender;

    @Autowired
    private transient HashMap<EventType, MailTemplate> messageTemplates;

    private final transient List<EventType> eventTypesUserRelated = Arrays.asList(
            EventType.SIGN_UP, EventType.TEST,
            EventType.RESET_PASSWORD, EventType.RESET_PASSWORD_CONFIRM);

    //@Value("${application.properties.test-mode:false}")
    //private transient String testMode;

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
        //System.out.println(testMode + " for service");
        SimpleMailMessage message = new SimpleMailMessage();
        String activityDetails;

        message.setFrom(Objects.requireNonNull(((JavaMailSenderImpl) mailSender).getUsername()));
        message.setTo(notificationDTO.getUserDTO().getEmail());
        message.setSubject(messageTemplates.get(notificationDTO.getEventType()).getSubject());

        EventType eventType = notificationDTO.getEventType();

        if (eventTypesUserRelated.contains(eventType)) {
            message.setText(messageTemplates.get(notificationDTO.getEventType()).getMessage());
        } else {
            activityDetails = formatActivityDetailsMessage(notificationDTO);
            message.setText(messageTemplates.get(notificationDTO.getEventType()).getMessage()
                    + activityDetails);
        }
        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new MailNotSentException(e.getMessage());
        }

        return message;
    }

    private String formatActivityDetailsMessage(NotificationDTO notificationDTO) {
        ActivityDTO activityDTO = notificationDTO.getActivityDTO();
        String message = "\nActivity Details:\nDate: " + activityDTO.getStartTime()
                + " - " + activityDTO.getEndTime() + "\nLocation: "
                + activityDTO.getLocation() + "\nHosted by: "
                + activityDTO.getOwner();

        if (activityDTO.getClass() == CompetitionDTO.class) {
            message += "\nFor: " + ((CompetitionDTO) activityDTO).getRequiredGender()
                    + "\nInvited organization: " + ((CompetitionDTO) activityDTO).getRequiredOrganization();
        }
        if (notificationDTO.getOptionalField() != null) {
            message += "\n\n" + notificationDTO.getOptionalField();
        }
        return message;
    }
}
