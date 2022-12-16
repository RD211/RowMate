package nl.tudelft.sem.project.matchmaking.services;

import nl.tudelft.sem.project.entities.activities.ActivitiesFeignClient;
import nl.tudelft.sem.project.entities.activities.ActivityDTO;
import nl.tudelft.sem.project.entities.utils.ActivityDeregisterRequestDTO;
import nl.tudelft.sem.project.entities.utils.ActivityRegistrationRequestDTO;
import nl.tudelft.sem.project.entities.utils.ActivityRequestDTO;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistration;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistrationId;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class MatchmakingService {

    transient ActivitiesFeignClient activitiesClient;
    transient ActivityRegistrationRepository activityRegistrationRepository;

    @Autowired
    public MatchmakingService(
            ActivitiesFeignClient activitiesClient,
            ActivityRegistrationRepository activityRegistrationRepository
    ) {
        this.activitiesClient = activitiesClient;
        this.activityRegistrationRepository = activityRegistrationRepository;
    }

    public ResponseEntity<List<ActivityDTO>> findActivities(ActivityRequestDTO dto) {
        List<ActivityDTO> result = activitiesClient.findActivitiesFromFilter(dto.getActivityFilter());
        return new ResponseEntity<List<ActivityDTO>>(result, HttpStatus.OK);
    }

    /**
     * Registers a user in an activity.
     *
     * @param dto a DTO containing the information needed for the request.
     * @return true if the registration was successful, e.g. the user is not already registered in the same activity
     *      and the role in the specified boat is available, false otherwise.
     */
    @Transactional
    public boolean registerUserInActivity(ActivityRegistrationRequestDTO dto) {
        List<ActivityRegistration> overlappingRegistrations
            = activityRegistrationRepository.findRequestOverlap(
                    dto.getActivityId(),
                    dto.getUserName(),
                    dto.getBoat(),
                    dto.getBoatRole()
            );

        if (!overlappingRegistrations.isEmpty()) {
            return false;
        }

        ActivityRegistration registration
            = new ActivityRegistration(
                dto.getUserName(),
                dto.getActivityId(),
                dto.getBoat(),
                dto.getBoatRole()
            );
        activityRegistrationRepository.save(registration);
        return true;
    }


    /**
     * De-registers a user from an activity.
     *
     * @param dto a DTO containg the username and activity id.
     * @return true if the de-register was succesful, e.g. the user was indeed registered in the activity,
     *      false otherwise.
     */
    @Transactional
    public boolean deRegisterUserFromActivity(ActivityDeregisterRequestDTO dto) {
        Optional<ActivityRegistration> registration
            = activityRegistrationRepository.findById(new ActivityRegistrationId(dto.getUserName(), dto.getActivityId()));
        if (registration.isEmpty()) {
            return false;
        }

        activityRegistrationRepository.delete(registration.get());
        return true;
    }


}
