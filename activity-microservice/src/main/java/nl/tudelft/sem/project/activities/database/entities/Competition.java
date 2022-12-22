package nl.tudelft.sem.project.activities.database.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.tudelft.sem.project.enums.Gender;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * A Competition is one of the two different types of activities.
 * It will have a 'C' in the discriminator column activity_type.
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@DiscriminatorValue("C")
public class Competition extends Activity {

    /**
     * Determines if the competitions is available for amateurs.
     */
    @Column()
    Boolean allowsAmateurs;
    /**
     * The Organization of the competition.
     */
    @Column()
    String requiredOrganization;

    /**
     * Gender of person who wants to join the competition.
     */
    @Column()
    protected Gender requiredGender;

    /**
     * The competition constructor.
     *
     * @param id the id of the competition.
     * @param location the location.
     * @param owner the activity owner.
     * @param startTime the start time of the competition.
     * @param endTime the end time of the competition.
     * @param boats a list of boats that are part of the activity.
     * @param allowsAmateurs if the competition allows amateurs.
     * @param requiredOrganization the required organization.
     * @param requiredGender the required gender.
     */
    public Competition(UUID id,
                       String location,
                       String owner,
                       LocalDateTime startTime,
                       LocalDateTime endTime,
                       List<Boat> boats,
                       Boolean allowsAmateurs,
                       String requiredOrganization,
                       Gender requiredGender) {
        super(id, location, owner, startTime, endTime, boats);
        this.allowsAmateurs = allowsAmateurs;
        this.requiredOrganization = requiredOrganization;
        this.requiredGender = requiredGender;
    }
}
