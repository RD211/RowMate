package nl.tudelft.sem.project.matchmaking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.enums.BoatRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityApplicationModel {
    protected String username;
    protected int boatIndex;
    protected BoatRole boatRole;
}