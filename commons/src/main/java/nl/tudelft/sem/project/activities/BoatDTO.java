package nl.tudelft.sem.project.activities;

import lombok.*;
import nl.tudelft.sem.project.DTO;
import nl.tudelft.sem.project.enums.BoatRole;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
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
@Validated
public class BoatDTO implements DTO {

    /**
     * The id of the boat.
     */
    protected UUID boatId;

    /**
     * The name of the boat.
     */
    @Valid @NotNull
    @Size(min = 4, max = 50, message = "The name of the boat must be between 4 and 50 characters.")
    protected String name;

    /**
     * A list of all available positions for this boat. There can be duplicates
     * if there's multiple of a role needed.
     */
    @Valid @NotNull
    protected List<BoatRole> availablePositions;

    /**
     * Id of the certificate that is required for a cox to be able to operate
     * the boat.
     */
    @Valid @NotNull
    protected UUID coxCertificateId;

}
