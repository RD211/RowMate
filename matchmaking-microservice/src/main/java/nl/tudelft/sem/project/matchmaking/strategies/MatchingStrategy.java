package nl.tudelft.sem.project.matchmaking.strategies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.project.entities.utils.ActivityRegistrationRequestDTO;
import nl.tudelft.sem.project.entities.utils.ActivityRequestDTO;
import nl.tudelft.sem.project.matchmaking.models.AvailableActivityModel;
import nl.tudelft.sem.project.matchmaking.models.FoundActivityModel;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class MatchingStrategy {
    private ActivityRequestDTO requestData;
    private List<AvailableActivityModel> availableActivities;

    public abstract FoundActivityModel findActivityToRegister();

    protected FoundActivityModel createResultFromPick(AvailableActivityModel pick) {
        ActivityRegistrationRequestDTO requestDTO =
                ActivityRegistrationRequestDTO
                    .builder()
                    .userName(requestData.getUserName())
                    .activityId(pick.getActivityDTO().getId())
                    .boat(pick.getBoat())
                    .boatRole(pick.getRole())
                    .build();

        return FoundActivityModel.builder()
                .registrationRequestDTO(requestDTO)
                .activityDTO(pick.getActivityDTO())
                .build();
    }
}
