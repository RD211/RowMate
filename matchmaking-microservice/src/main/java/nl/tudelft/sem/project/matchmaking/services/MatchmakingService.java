package nl.tudelft.sem.project.matchmaking.services;

import nl.tudelft.sem.project.entities.activities.ActivitiesFeignClient;
import nl.tudelft.sem.project.entities.activities.ActivityDTO;
import nl.tudelft.sem.project.entities.utils.ActivityRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchmakingService {

    transient ActivitiesFeignClient activitiesClient;

    @Autowired
    public MatchmakingService(ActivitiesFeignClient activitiesClient) {
        this.activitiesClient = activitiesClient;
    }

    public ResponseEntity<List<ActivityDTO>> findActivities(ActivityRequestDTO dto) {
        List<ActivityDTO> result = activitiesClient.findActivitiesFromFilter(dto.getActivityFilter());

        return new ResponseEntity<List<ActivityDTO>>(result, HttpStatus.OK);
    }
}
