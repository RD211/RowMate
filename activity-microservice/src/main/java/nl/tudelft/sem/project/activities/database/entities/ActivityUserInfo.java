package nl.tudelft.sem.project.activities.database.entities;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import nl.tudelft.sem.project.enums.BoatRole;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Embeddable
@Value
@RequiredArgsConstructor
public class ActivityUserInfo {

    /**
     * The user for whom this info is about.
     */
    protected UUID userID;

    /**
     * The boat ID that the user is assigned to. If null that means the user has not
     * yet been accepted into the activity.
     */
    protected UUID boatId;

    /**
     * The role that the user wants to be as in the activity.
     */
    @Enumerated(value = EnumType.STRING)
    protected BoatRole requestedRole;


}
