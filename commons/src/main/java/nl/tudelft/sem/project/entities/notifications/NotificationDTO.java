package nl.tudelft.sem.project.entities.notifications;

import lombok.*;
import nl.tudelft.sem.project.entities.activities.ActivityDTO;
import nl.tudelft.sem.project.entities.users.UserDTO;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public abstract class NotificationDTO {

    /**
     * The user DTO containing user ID, username, and e-mail address
     */
    @NonNull
    protected UserDTO userDTO;

    /**
     * The activity DTO containing details about the
     * activity the notification should be for
     */
    @NonNull
    protected ActivityDTO activityDTO;

    /**
     * The event the notification refers to (reset password,
     * join activity, etc.)
     */
    @NonNull
    protected EventType eventType;
}
