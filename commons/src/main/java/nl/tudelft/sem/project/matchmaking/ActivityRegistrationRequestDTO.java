package nl.tudelft.sem.project.matchmaking;

import lombok.*;
import nl.tudelft.sem.project.enums.BoatRole;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRegistrationRequestDTO {
    protected String userName;
    protected UUID activityId;
    protected int boat;
    protected BoatRole boatRole;
}
