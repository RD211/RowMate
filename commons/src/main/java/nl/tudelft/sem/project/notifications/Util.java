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

    public static String formatActivityDetailsMessage(NotificationDTO notificationDTO) {
        if (!eventTypesUserRelated.contains(notificationDTO.getEventType())) {
            return "";
        }

        ActivityDTO activityDTO = notificationDTO.getActivityDTO();
        String message = "\nActivity Details:\nDate: " + activityDTO.getStartTime()
                + " - " + activityDTO.getEndTime() + "\nLocation: "
                + activityDTO.getLocation() + "\nHosted by: "
                + activityDTO.getOwner();
        String optionalField = notificationDTO.getOptionalField();

        if (activityDTO.getClass() == CompetitionDTO.class) {
            message += "\nFor: " + ((CompetitionDTO) activityDTO).getRequiredGender()
                    + "\nInvited organization: " + ((CompetitionDTO) activityDTO).getRequiredOrganization();
        }
        message += "\n\n" + (optionalField != null ? optionalField : "");
        return message;
    }
}
