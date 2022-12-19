package nl.tudelft.sem.project.matchmaking.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.enums.BoatRole;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AvailableActivityModel {
    private ActivityDTO activityDTO;
    private int boat;
    private BoatRole role;
}
