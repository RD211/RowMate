package nl.tudelft.sem.project.gateway;

import lombok.*;
import nl.tudelft.sem.project.enums.BoatRole;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class SeatedUserModel {
    protected UUID activityId;
    protected int boat;
    protected BoatRole boatRole;
}
