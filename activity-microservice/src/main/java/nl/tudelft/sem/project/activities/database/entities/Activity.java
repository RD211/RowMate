package nl.tudelft.sem.project.activities.database.entities;

import lombok.*;
import nl.tudelft.sem.project.entities.DTOable;
import nl.tudelft.sem.project.entities.activities.ActivityDTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * The activity entity.
 * The training and competition entities will inherit from activity.
 * In the database schema, we will use the same table for all three, and discriminate between them
 * by using the activity_type column, which will be 'T' for trainings and 'C' for competitions.
 *
 * <p>An activity is an organised event that has an owner, boats that require
 * certain roles to be filled and users that are, or want to participate in
 * the activity.</p>
 *
 */

@Data
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Entity
@Table(name = "activities")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "activity_type")
public class Activity implements DTOable<ActivityDTO> {
    /**
     * The unique identifier of the training.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    /**
     * The location of the activity.
     */
    String location;

    /**
     * Start and end times of the activity.
     */
    @Column(name = "start_time", columnDefinition = "TIMESTAMP")
    protected LocalDateTime startTime;

    @Column(name = "end_time", columnDefinition = "TIMESTAMP")
    protected LocalDateTime endTime;

    /**
     * List of boats available for the activity.
     */
    @Column(nullable = false)
    @ElementCollection
    protected List<UUID> boats;

    /**
     * Creates an activity entity from a DTO.
     *
     * @param dto the DTO to create the entity from.
     */
    public Activity(ActivityDTO dto) {
        this(dto.getId(), dto.getLocation(), dto.getStartTime(), dto.getEndTime(), dto.getBoats());
    }


    @Override
    public ActivityDTO toDTO() {
        return new ActivityDTO(this.id, this.location, this.startTime, this.endTime, this.boats);
    }
}
