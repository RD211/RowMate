package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.activities.ActivitiesClient;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.gateway.SeatedUserModel;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
import nl.tudelft.sem.project.matchmaking.*;
import nl.tudelft.sem.project.notifications.NotificationClient;
import nl.tudelft.sem.project.users.UsersClient;
import nl.tudelft.sem.project.utils.Fictional;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matchmaking")
public class MatchmakingController {

    private final transient MatchmakingClient matchmakingClient;

    private final transient ActivitiesClient activitiesClient;

    private final transient UsersClient usersClient;

    private final transient NotificationClient notificationClient;

    private final transient AuthManager authManager;

    /**
     * The matchmaking controller constructor.
     *
     * @param matchmakingClient the matchmaking client.
     * @param usersClient the user client.
     * @param authManager the auth manager.
     */
    @Autowired
    public MatchmakingController(
            MatchmakingClient matchmakingClient,
            UsersClient usersClient,
            AuthManager authManager,
            NotificationClient notificationClient,
            ActivitiesClient activitiesClient) {
        this.matchmakingClient = matchmakingClient;
        this.usersClient = usersClient;
        this.authManager = authManager;
        this.notificationClient = notificationClient;
        this.activitiesClient = activitiesClient;
    }

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

    /**
     * Registers the user to an activity given the activity, role and boat.
     *
     * @param seatedUserModel the model that contains the details.
     * @return a confirmation string.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SeatedUserModel seatedUserModel) {
        var username = authManager.getUsername();

        var activityRequest = new ActivityRegistrationRequestDTO();
        activityRequest.setActivityId(seatedUserModel.getActivityId());
        activityRequest.setBoatRole(seatedUserModel.getBoatRole());
        activityRequest.setUserName(username);
        activityRequest.setBoat(seatedUserModel.getBoat());

        activitiesClient.getActivity(activityRequest.getActivityId());

        return ResponseEntity.ok(matchmakingClient.registerInActivity(activityRequest));
    }

    /**
     * Responds to a request to join an activity. Only the owner of the activity can
     * accept/reject a registration request.
     *
     * @param response a dto, containing the username of the user who wants to join the activity,
     *                 the activity id, and a boolean indicating whether the request is accepted
     *                 or not.
     * @return a confirmation string.
     */
    @PostMapping("/respond")
    public ResponseEntity<String> respondToRegistration(@RequestBody ActivityRegistrationResponseDTO response) {
        var username = authManager.getUsername();

        ActivityDTO activity = activitiesClient.getActivity(response.getActivityId());

        if (!username.equals(activity.getOwner())) {
            throw new IllegalArgumentException("You are not the owner of this activity!");
        }

        return ResponseEntity.ok(matchmakingClient.respondToRegistration(response));
    }

    /**
     * DeRegisters the user from an activity.
     *
     * @param activityId the id of the activity.
     * @return a confirmation string.
     */
    @PostMapping("/deregister")
    public ResponseEntity<String> deregister(@RequestBody UUID activityId) {
        var username = authManager.getUsername();
        var activityRequest = new ActivityDeregisterRequestDTO();

        activityRequest.setActivityId(activityId);
        activityRequest.setUserName(username);

        activitiesClient.getActivity(activityId);
        return ResponseEntity.ok(matchmakingClient.deRegisterFromActivity(activityRequest));
    }


    /**
     * Gets the waiting applications of the currently signed in user.
     *
     * @return the list of seated applications.
     */
    @GetMapping("/get_waiting_applications")
    public ResponseEntity<List<UserActivityApplication>> getWaitingApplications() {
        var username = authManager.getUsername();

        return ResponseEntity.ok(matchmakingClient.getWaitingApplications(username));
    }

    /**
     * Gets the accepted applications of the currently signed in user.
     *
     * @return the list of seated applications.
     */
    @GetMapping("/get_accepted_applications")
    public ResponseEntity<List<UserActivityApplication>> getAcceptedApplications() {
        var username = authManager.getUsername();

        return ResponseEntity.ok(matchmakingClient.getAcceptedApplications(username));
    }
}
