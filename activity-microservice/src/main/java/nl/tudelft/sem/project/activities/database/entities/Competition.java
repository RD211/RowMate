package nl.tudelft.sem.project.activities.database.entities;

import lombok.*;
import nl.tudelft.sem.project.entities.activities.CompetitionDTO;
import nl.tudelft.sem.project.enums.Gender;
import javax.persistence.*;

/**
 * A Competition is one of the two different types of activities.
 * It will have a 'C' in the discriminator column activity_type.
 */

@Data
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Entity
@DiscriminatorValue("C")
public class Competition extends Activity {

    /**
     * Determines if the competitions is available for amateurs.
     */
    @Column(nullable = false)
    Boolean allowsAmateurs;
    /**
     * The Organization of the competition.
     */
    @Column(nullable = false)
    String requiredOrganization;

    /**
     * Gender of person who wants to join the competition.
     */
    @Column(nullable = false, unique = true)
    protected Gender requiredGender;

    @Override
    public CompetitionDTO toDTO() {
        return new CompetitionDTO(
                this.getId(),
                this.getLocation(),
                this.getStartTime(),
                this.getEndTime(),
                this.getBoats(),
                this.getAllowsAmateurs(),
                this.getRequiredOrganization(),
                this.getRequiredGender());
    }
}
