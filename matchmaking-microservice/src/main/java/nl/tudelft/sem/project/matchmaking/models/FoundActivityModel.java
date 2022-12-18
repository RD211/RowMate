package nl.tudelft.sem.project.matchmaking.models;

import lombok.*;
import nl.tudelft.sem.project.entities.activities.ActivityDTO;
import nl.tudelft.sem.project.entities.utils.ActivityRegistrationRequestDTO;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class FoundActivityModel {
    private ActivityRegistrationRequestDTO registrationRequestDTO;
    private ActivityDTO activityDTO;
}
