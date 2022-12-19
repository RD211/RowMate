package nl.tudelft.sem.project.activities.database.entities;

import lombok.*;
import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.enums.Gender;
import javax.persistence.*;

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
@DiscriminatorValue("C")
public class Competition extends Activity {

    /**
     * Determines if the competitions is available for amateurs.
     */
    @Column(nullable = true)
    Boolean allowsAmateurs;
    /**
     * The Organization of the competition.
     */
    @Column(nullable = true)
    String requiredOrganization;

    /**
     * Gender of person who wants to join the competition.
     */
    @Column(nullable = true, unique = true)
    protected Gender requiredGender;

    /**
     * Creates a competition entity from a DTO.
     *
     * @param dto the DTO to create the entity from.
     */
    public Competition(CompetitionDTO dto) {
        super(dto);
        this.allowsAmateurs = dto.getAllowsAmateurs();
        this.requiredOrganization = dto.getRequiredOrganization();
        this.requiredGender = dto.getRequiredGender();
    }

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
