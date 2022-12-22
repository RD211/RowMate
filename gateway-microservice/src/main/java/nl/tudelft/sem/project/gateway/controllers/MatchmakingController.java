package nl.tudelft.sem.project.gateway.controllers;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    private final transient NotificationsClient notificationsClient;

    /**
     * The matchmaking controller constructor.
     *
     * @param matchmakingClient the matchmaking client.
     * @param usersClient the user client.
     * @param authManager the auth manager.
     */
    @Autowired
    public MatchmakingController(MatchmakingClient matchmakingClient, UsersClient usersClient, AuthManager authManager,
                                 ActivitiesClient activitiesClient, NotificationsClient notificationsClient) {
        this.matchmakingClient = matchmakingClient;
        this.usersClient = usersClient;
        this.authManager = authManager;
        this.notificationClient = notificationClient;
        this.activitiesClient = activitiesClient;
        this.notificationsClient = notificationsClient;
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

        ActivityDTO activityDTO = activitiesClient.getActivity(activityRequest.getActivityId());

        // Send notifications to user who has joined and to the owner of the activity.
        UserDTO userDTO = usersClient.getUserByUsername(new Username(username));
        UserDTO ownerUserDTO = usersClient.getUserByUsername(new Username(activityDTO.getOwner()));
        notificationsClient.sendNotification(NotificationDTO.builder()
                .userDTO(userDTO)
                .activityDTO(activityDTO)
                .eventType(EventType.JOINED_ACTIVITY)
                .build());
        notificationsClient.sendNotification(NotificationDTO.builder()
                .userDTO(ownerUserDTO)
                .activityDTO(activityDTO)
                .eventType(EventType.USER_JOINED)
                .build());

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

        ActivityDTO activityDTO = activitiesClient.getActivity(activityId);

        // Send notification to activity owner that the user has left.
        UserDTO ownerUserDTO = usersClient.getUserByUsername(new Username(activityDTO.getOwner()));
        notificationsClient.sendNotification(NotificationDTO.builder()
                .userDTO(ownerUserDTO)
                .activityDTO(activityDTO)
                .eventType(EventType.USER_LEFT)
                .build());

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
