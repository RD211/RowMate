package nl.tudelft.sem.project.activities.services;

import nl.tudelft.sem.project.activities.database.entities.Activity;
import nl.tudelft.sem.project.activities.database.repository.ActivityRepository;
import nl.tudelft.sem.project.entities.activities.ActivityDTO;
import nl.tudelft.sem.project.entities.utils.ActivityFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {


    transient ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<ActivityDTO> findActivitiesFromFilter(ActivityFilterDTO dto) {
        return this.toDTOs(activityRepository.findAll());
    }

    /**
     * Creates a list of DTOs from a list of entities.
     *
     * @param activities the list of entities
     * @return a list of DTOs corresponding to the activities.
     */
    public List<ActivityDTO> toDTOs(List<Activity> activities) {
        List<ActivityDTO> result = new ArrayList<>(activities.size());
        for (var activity : activities) {
            result.add(activity.toDTO());
        }

        return result;
    }
}
