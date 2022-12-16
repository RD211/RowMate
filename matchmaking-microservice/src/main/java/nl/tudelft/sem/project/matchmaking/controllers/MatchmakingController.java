package nl.tudelft.sem.project.matchmaking.controllers;

import nl.tudelft.sem.project.entities.activities.ActivityDTO;
import nl.tudelft.sem.project.entities.utils.ActivityDeregisterRequestDTO;
import nl.tudelft.sem.project.entities.utils.ActivityRegistrationRequestDTO;
import nl.tudelft.sem.project.entities.utils.ActivityRequestDTO;
import nl.tudelft.sem.project.matchmaking.services.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("find")
    public ResponseEntity<List<ActivityDTO>> findActivities(@RequestBody ActivityRequestDTO dto) {
        return matchmakingService.findActivities(dto);
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
