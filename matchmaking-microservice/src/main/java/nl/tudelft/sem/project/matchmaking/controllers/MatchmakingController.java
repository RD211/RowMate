package nl.tudelft.sem.project.matchmaking.controllers;

import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.matchmaking.*;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.matchmaking.ActivityDeregisterRequestDTO;
import nl.tudelft.sem.project.matchmaking.ActivityRegistrationRequestDTO;
import nl.tudelft.sem.project.matchmaking.ActivityRequestDTO;
import nl.tudelft.sem.project.matchmaking.UserActivityApplication;
import nl.tudelft.sem.project.matchmaking.services.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matchmaking/")
public class MatchmakingController {

    transient MatchmakingService matchmakingService;

    @Autowired
    public MatchmakingController(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    /**
     * This endpoint will return all the activities that a user can participate in.
     *
     * @param dto the DTO containing all the data needed by the endpoint.
     * @return a list of activities, representing the activities the user can take part in.
     */
    @PostMapping("list")
    public ResponseEntity<List<ActivityDTO>> findActivities(@RequestBody ActivityRequestDTO dto) {
        return new ResponseEntity<>(matchmakingService.findActivities(dto), HttpStatus.OK);
    }

    /**
     * Given a user, the time interval and a list of preferred roles,
     * this endpoint finds an activity for the user automatically.
     *
     * @param strategy the strategy that the matchmaker should use. right now,
     *        1 will find the user the earliest available strategy
     *        and 2 will just assign the user to a random one.
     * @param dto the dto containing the data
     * @return a response string, informing the user of the activity he was assigned to.
     */
    @PostMapping("/find/{strategy}")
    public ResponseEntity<String> autoFindActivity(
         @PathVariable("strategy") MatchmakingStrategy strategy,
        @RequestBody ActivityRequestDTO dto
    ) {
        return ResponseEntity.ok(matchmakingService.autoFindActivity(strategy, dto));
    }

    /**
     * Registers a user to an activity, on the specified boat and role.
     *
     * @param dto a DTO containing the username, activity id, boat index and role.
     * @return a response message, indicating whether registration was successful or not.
     */
    @PostMapping("register")
    public ResponseEntity<String> registerInActivity(@RequestBody ActivityRegistrationRequestDTO dto) {
        boolean result = matchmakingService.registerUserInActivity(dto);
        if (result) {
            return ResponseEntity.ok("Registration successful.");
        }

        throw new RuntimeException(
                "There was a problem registering in the activity."
                + "Either you are already registered to it, or the role in the boat is taken."
        );
    }

    /**
     * De-registers a user from an activity.
     *
     * @param dto a DTO containing the username and activity id.
     * @return a response message, indicating whether the de-registration was succesful or not.
     */
    @PostMapping("deregister")
    public ResponseEntity<String> deRegisterFromActivity(@RequestBody ActivityDeregisterRequestDTO dto) {
        boolean result = matchmakingService.deregisterUserFromActivity(dto);
        if (result) {
            return ResponseEntity.ok("You successfully deregistered.");
        }

        throw new RuntimeException("You are not enrolled in the activity!");
    }

    /**
     * Responds to an activity registration request. If the request is accepted,
     * the registration is marked as "accepted".
     * The user will be notified about the response.
     *
     * @param dto a DTO containing the username, activity id and response.
     * @return a string, containing the response.
     */
    @PostMapping("respond")
    public ResponseEntity<String> respondToRegistration(@RequestBody ActivityRegistrationResponseDTO dto) {
        boolean result = matchmakingService.respondToRegistration(dto);

        if (result) {
            return ResponseEntity.ok("Your response has been processed and the user has been notified.");
        }

        throw new RuntimeException("There was an error processing your response.");
    }

    /**
     * Get waiting applications. Gets a list of seated user model of where the user applied to and has not yet gotten
     * approved.
     *
     * @param username the username.
     * @return the list of models.
     */
    @GetMapping("get_waiting_applications")
    public ResponseEntity<List<UserActivityApplication>> getWaitingApplications(
            @RequestParam(value = "username") String username) {
        return ResponseEntity.ok(matchmakingService.getAllActivitiesThatUserAppliedTo(username));
    }

    /**
     * Get accepted applications. Gets a list of seated user model of where the user applied to and got accepted.
     *
     * @param username the username.
     * @return the list of models.
     */
    @GetMapping("get_accepted_applications")
    public ResponseEntity<List<UserActivityApplication>> getAcceptedApplications(
            @RequestParam(value = "username") String username) {
        return ResponseEntity.ok(matchmakingService.getAllActivitiesThatUserIsPartOf(username));
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
        matchmakingService.deleteUserFromActivity(userName, activityId);
        return ResponseEntity.ok().build();
    }

    /**
     * Gets all applications to an activity by status.
     * For example, you could get all accepted applications which means
     * already part of the activity. But also you could get people in the waiting
     * area.
     *
     * @param activityId the activity id.
     * @param accepted the status.
     * @return the list of applications.
     */
    @GetMapping("/get_applications_for_activity_by_status")
    public ResponseEntity<List<ActivityApplicationModel>> getApplicationsForActivityByStatus(
            @RequestParam @NotNull @Valid UUID activityId,
            @RequestParam @NotNull @Valid boolean accepted) {
        return ResponseEntity.ok(
                matchmakingService.getAllApplicationsToActivityByAcceptedStatus(activityId, accepted));
    }
}
