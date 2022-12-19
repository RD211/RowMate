package nl.tudelft.sem.project.gateway;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.project.enums.BoatRole;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatedUserModel {
    protected UUID activityId;
    protected int boat;
    protected BoatRole boatRole;
}
