package nl.tudelft.sem.project.activities.database.entities;

import nl.tudelft.sem.project.enums.BoatRole;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Embeddable
public class ActivityUserInfo {

    /**
     * The user for whom this info is about.
     */
    @Column(nullable = false, unique = true)
    protected UUID userID;

    /**
     * The boat ID that the user is assigned to. If null that means the user has not
     * yet been accepted into the activity.
     */
    @Column(nullable = false)
    protected UUID boatId;

    /**
     * The role that the user wants to be as in the activity.
     */
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    protected BoatRole requestedRole;

}
