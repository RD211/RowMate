package nl.tudelft.sem.project.gateway.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import nl.tudelft.sem.project.activities.ActivitiesClient;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.gateway.SeatedUserModel;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
import nl.tudelft.sem.project.matchmaking.*;
import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.NotificationDTO;
import nl.tudelft.sem.project.notifications.NotificationsClient;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/registrations")
public class ActivityRegistrationController {
    @Autowired
    private transient MatchmakingClient matchmakingClient;

    @Autowired
    private transient ActivitiesClient activitiesClient;

    @Autowired
    private transient UsersClient usersClient;

    @Autowired
    private transient AuthManager authManager;

    @Autowired
    private transient NotificationsClient notificationsClient;

    /**
     * Gets all participants to an activity.
     *
     * @param activityId the activity id.
     * @return the list of participants.
     */
    @GetMapping("/get_participants")
    public ResponseEntity<List<ActivityApplicationModel>> getParticipants(
            @RequestParam @NotNull @Valid UUID activityId) {
        return ResponseEntity.ok(
                matchmakingClient.getApplicationsForActivityByStatus(activityId, true));
    }

    /**
     * Gets all people that are in the waiting room for an activity.
     *
     * @param activityId the activity id.
     * @return the waiting room.
     */
    @GetMapping("/get_waiting_room")
    public ResponseEntity<List<ActivityApplicationModel>> getWaitingRoom(
            @RequestParam @NotNull @Valid UUID activityId) {
        return ResponseEntity.ok(
                matchmakingClient.getApplicationsForActivityByStatus(activityId, false));
    }

    /**
     * Registers the user to an activity given the activity, role and boat.
     * The boat is given as the index of the boat in the activity. Giving a value that is out the range
     * of the list will result in a 400 error.
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

        try {
            new Thread(() -> {
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
            }).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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
        try {
            new Thread(() -> {
                UserDTO ownerUserDTO = usersClient.getUserByUsername(new Username(activityDTO.getOwner()));
                notificationsClient.sendNotification(NotificationDTO.builder()
                        .userDTO(ownerUserDTO)
                        .activityDTO(activityDTO)
                        .eventType(EventType.USER_LEFT)
                        .build());
            }).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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

    /**
     * Deletes a user from an activity.
     *
     * @param activityId The ID of the activity.
     * @param userName The name of the user to be removed.
     * @return 200 if everything went ok.
     */
    @DeleteMapping("/delete_user_from_activity")
    public ResponseEntity<Void> deleteByUserNameAndActivityId(
            @RequestParam @NotNull @Valid UUID activityId,
            @RequestParam @NotNull @Valid String userName
    ) {
        var activity = activitiesClient.getActivity(activityId);
        if (!authManager.getUsername().equals(activity.getOwner())) {
            throw new RuntimeException("You are not the owner of this activity!");
        }
        matchmakingClient.deleteByUserNameAndActivityId(activityId, userName);
        return ResponseEntity.ok().build();
    }
}
