package nl.tudelft.sem.project.matchmaking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.enums.BoatRole;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityApplication {
    protected ActivityDTO activityDTO;
    protected BoatDTO boat;
    protected BoatRole boatRole;
}
