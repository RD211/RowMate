package nl.tudelft.sem.project.gateway.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import nl.tudelft.sem.project.activities.ActivitiesClient;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.gateway.SeatedUserModel;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
import nl.tudelft.sem.project.matchmaking.*;
import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.NotificationsClient;
import nl.tudelft.sem.project.notifications.NotificationDTO;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/matchmaking")
public class MatchmakingController {

    @Autowired
    private transient MatchmakingClient matchmakingClient;

    @Autowired
    private transient AuthManager authManager;

    /**
     * Endpoint for finding activities.
     * This will search for activities given the filter.
     *
     * @param activityFilterDTO the filter.
     * @return the list of activities.
     */
    @PostMapping("/list")
    public ResponseEntity<List<ActivityDTO>> findActivities(@Valid @RequestBody
                                                      ActivityFilterDTO activityFilterDTO) {
        var username = authManager.getUsername();



        return ResponseEntity.ok(matchmakingClient.findActivities(
                new ActivityRequestDTO(username, activityFilterDTO)
        ));
    }

    /**
     * Find with strategy. Will automatically apply the user to an activity.
     *
     * @param strategy the strategy to search with.
     * @param activityFilterDTO the filter dto.
     * @return a confirmation string.
     */
    @PostMapping("/find/{strategy}")
    public ResponseEntity<String> autoFindActivity(@PathVariable(value = "strategy")
                                                                MatchmakingStrategy strategy,
                                                            @RequestBody ActivityFilterDTO activityFilterDTO) {
        var username = authManager.getUsername();

        return ResponseEntity.ok(matchmakingClient.autoFindActivity(
                strategy,
                new ActivityRequestDTO(username, activityFilterDTO)
        ));
    }
}
