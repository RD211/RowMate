package nl.tudelft.sem.project.notifications;

import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.CompetitionDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Util {

    private static final transient List<EventType> eventTypesUserRelated = Arrays.asList(
            EventType.SIGN_UP, EventType.TEST,
            EventType.RESET_PASSWORD, EventType.RESET_PASSWORD_CONFIRM);

    /**
     * Formats the message to be included in the notification email
     * @param notificationDTO the notification details
     * @return the complete notification message text
     */
    public static String formatActivityDetailsMessage(NotificationDTO notificationDTO) {
        if (eventTypesUserRelated.contains(notificationDTO.getEventType())) {
            return "";
        }
        ActivityDTO activityDTO = notificationDTO.getActivityDTO();
        String message = addActivityInfo(activityDTO);
        String optionalField = notificationDTO.getOptionalField();

        message += "\n\n" + (optionalField != null ? optionalField : "");
        return message;
    }

    /**
     * Adds in the details about the activity.
     * @param activityDTO the activity DTO
     * @return part of the message containing activity info
     */
    private static String addActivityInfo(ActivityDTO activityDTO) {
        String message;
        message = "\nActivity Details:\nDate: " + activityDTO.getStartTime()
                + " - " + activityDTO.getEndTime() + "\nLocation: "
                + activityDTO.getLocation() + "\nHosted by: "
                + activityDTO.getOwner();
        if (activityDTO.getClass() == CompetitionDTO.class) {
            message += "\nFor: " + ((CompetitionDTO) activityDTO).getRequiredGender()
                    + "\nInvited organization: " + ((CompetitionDTO) activityDTO).getRequiredOrganization();
        }
        return message;
    }
}
