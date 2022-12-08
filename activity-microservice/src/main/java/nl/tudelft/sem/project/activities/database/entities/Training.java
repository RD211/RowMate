package nl.tudelft.sem.project.activities.database.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * The activity entity.
 *
 * <p>An activity is an organised event that has an owner, boats that require
 * certain roles to be filled and users that are, or want to participate in
 * the activity.</p>
 */
@Getter
@Setter
@ToString
@Entity
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Training implements Activity {

    /**
     * The unique identifier of the training.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID trainingId;

    /**
     * The ID of the activity owner.
     */
    @Column(nullable = false)
    protected UUID activityOwner;

    /**
     * Map that refers every participant in the activity to their user info.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "userID", column = @Column(name = "participantUserID")),
            @AttributeOverride(name = "boatID", column = @Column(name = "participantBoatID")),
            @AttributeOverride(name = "requestedRole", column = @Column(name = "participantRequestedRole")),
    })
    protected List<ActivityUserInfo> participants;

    /**
     * List of boats available for the activity.
     */
    @Column(nullable = false)
    @ElementCollection
    protected List<UUID> boats;
}
