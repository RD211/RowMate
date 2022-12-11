package nl.tudelft.sem.project.matchmaking.controllers;

import nl.tudelft.sem.project.entities.activities.ActivityDTO;
import nl.tudelft.sem.project.entities.utils.ActivityRequestDTO;
import nl.tudelft.sem.project.matchmaking.services.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class MatchmakingController {

    transient MatchmakingService matchmakingService;

    @Autowired
    public MatchmakingController(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    /**
     * This endpoint will return all the activities that a user can participate in.
     *
     * @param requestDTO the DTO containing all the data needed by the endpoint.
     * @return a list of activities, represing the activities the user can take part in.
     */
    @PostMapping("/activity/find")
    public ResponseEntity<List<ActivityDTO>> findActivities(@RequestBody  ActivityRequestDTO requestDTO) {
        return matchmakingService.findActivities(requestDTO);
    }
}
