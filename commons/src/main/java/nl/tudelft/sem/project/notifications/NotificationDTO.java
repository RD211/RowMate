package nl.tudelft.sem.project.notifications;

import lombok.*;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.users.UserDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class NotificationDTO {

    /**
     * The user DTO containing user ID, username, and e-mail address
     */
    @NonNull
    protected UserDTO userDTO;

    /**
     * The activity DTO containing details about the
     * activity the notification should be for
     */
    protected ActivityDTO activityDTO;

    /**
     * The event the notification refers to (reset password,
     * join activity, etc.)
     */
    @NonNull
    protected EventType eventType;
}
