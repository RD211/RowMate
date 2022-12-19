package nl.tudelft.sem.project.activities;

import lombok.*;
import nl.tudelft.sem.project.DTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.utils.Existing;
import nl.tudelft.sem.project.utils.Fictional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * DTO for Boat entities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public class BoatDTO implements DTO {

    /**
     * The id of the boat.
     */
    @NotNull(groups = Existing.class)
    protected UUID boatId;

    /**
     * The name of the boat.
     */
    @NotNull(groups = Fictional.class)
    @Size(min = 4, max = 50, message = "The name of the boat must be beteen 4 and 50 characters.")
    protected String name;

    /**
     * A list of all available positions for this boat. There can be duplicates
     * if there's multiple of a role needed.
     */
    @NotNull(groups = Fictional.class)
    protected List<BoatRole> availablePositions;

    /**
     * Id of the certificate that is required for a cox to be able to operate
     * the boat.
     */
    @NotNull(groups = Fictional.class)
    protected UUID coxCertificateId;

}
