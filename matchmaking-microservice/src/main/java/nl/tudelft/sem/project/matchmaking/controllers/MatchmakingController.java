package nl.tudelft.sem.project.matchmaking.controllers;

import nl.tudelft.sem.project.entities.activities.ActivityDTO;
import nl.tudelft.sem.project.entities.utils.ActivityDeregisterRequestDTO;
import nl.tudelft.sem.project.entities.utils.ActivityRegistrationRequestDTO;
import nl.tudelft.sem.project.entities.utils.ActivityRequestDTO;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.matchmaking.services.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
     * @return a list of activities, represing the activities the user can take part in.
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

        return ResponseEntity.ok(
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
        boolean result = matchmakingService.deRegisterUserFromActivity(dto);
        if (result) {
            return ResponseEntity.ok("You successfully deregistered.");
        }

        return ResponseEntity.ok("You are not enrolled in the activity!");
    }
}
