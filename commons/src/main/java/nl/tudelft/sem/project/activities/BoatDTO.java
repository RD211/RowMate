package nl.tudelft.sem.project.activities;

import lombok.*;
import nl.tudelft.sem.project.DTO;
import nl.tudelft.sem.project.enums.BoatRole;

import java.util.List;
import java.util.UUID;

/**
 * DTO for Boat entities.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class BoatDTO implements DTO {

    /**
     * The id of the boat.
     */
    protected UUID boatId;
    /**
     * The name of the boat.
     */
    @NonNull
    protected String name;
    /**
     * A list of all available positions for this boat. There can be duplicates
     * if there's multiple of a role needed.
     */
    @NonNull
    protected List<BoatRole> availablePositions;

}
