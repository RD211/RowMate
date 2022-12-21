package nl.tudelft.sem.project.activities.services;

import nl.tudelft.sem.project.activities.ActivityNotFoundException;
import nl.tudelft.sem.project.activities.database.entities.*;
import nl.tudelft.sem.project.activities.database.repository.ActivityRepository;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    transient ActivityRepository activityRepository;
    @Autowired
    transient BoatConverterService boatConverterService;

    final transient String couldNotFindActivityErrorMessage = "No activity could be found with this ID.";


    /**
     * Finds activities given a filterdto.
     *
     * @param dto the filter dto.
     * @return a list of activities matching that filter.
     */
    public List<Activity> findActivitiesFromFilter(ActivityFilterDTO dto) {
        var startTime = dto.getStartTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        var endTime = dto.getEndTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return activityRepository.findWithinTimeSlot(startTime, endTime);
    }

    /**
     * Gets an activity given the id.
     *
     * @param id the id of the activity.
     * @return the activity.
     */
    public Activity getActivityById(UUID id) {
        var optActivity = activityRepository.findById(id);
        if (optActivity.isEmpty()) {
            throw new ActivityNotFoundException(couldNotFindActivityErrorMessage);
        }

        return optActivity.get();
    }

    /**
     * Gets a training by id.
     *
     * @param id the id of the training.
     * @return the training.
     */
    public Training getTrainingById(UUID id) {
        var optTraining = activityRepository.findById(id);
        if (optTraining.isEmpty()) {
            throw new ActivityNotFoundException(couldNotFindActivityErrorMessage);
        }

        var training = optTraining.get();
        if (training instanceof Training) {
            return (Training) training;
        }
        throw new
                ActivityNotFoundException("Could not find training by id. Found different type of activity instead.");
    }

    /**
     * Gets a competition by id.
     *
     * @param id the id of the competition.
     * @return the competition.
     */
    public Competition getCompetitionById(UUID id) {
        var optCompetition = activityRepository.findById(id);
        if (optCompetition.isEmpty()) {
            throw new ActivityNotFoundException(couldNotFindActivityErrorMessage);
        }

        var competition = optCompetition.get();
        if (competition instanceof Competition) {
            return (Competition) competition;
        }
        throw new
                ActivityNotFoundException("Could not find competition by id. Found different type of activity instead.");
    }

    /**
     * Adds a training to the database.
     *
     * @param training the training.
     * @return the saved entity.
     */
    public Training addTraining(Training training) {
        return activityRepository.save(training);
    }

    /**
     * Adds a competition to the database.
     *
     * @param competition the competition.
     * @return the saved entity.
     */
    public Competition addCompetition(Competition competition) {
        return activityRepository.save(competition);
    }

    /**
     * Adds a new boat to an activity.
     *
     * @param activityId The activity to change.
     * @param boat The new boat to add. This boat is not validated to be present within
     *             the boat repository.
     * @return An activity object with the new boat added.
     * @throws ActivityNotFoundException Thrown if there is no such activity.
     */
    public Activity addBoatToActivity(UUID activityId, Boat boat) throws ActivityNotFoundException {
        var optActivity = activityRepository.findById(activityId);
        if (optActivity.isEmpty()) {
            throw new ActivityNotFoundException(couldNotFindActivityErrorMessage);
        }
        var activity = optActivity.get();
        activity.getBoats().add(boat);
        activityRepository.save(activity);
        return activity;
    }

    /**
     * Changes the start and end times of an activity.
     *
     * @param activityId The ID of the activity.
     * @param newStartTime New start time for the activity.
     * @param newEndTime New end time for the activity.
     * @return The activity after it has been changed.
     * @throws ActivityNotFoundException Thrown if there is no such activity.
     */
    public Activity changeActivityTimes(UUID activityId, Date newStartTime, Date newEndTime)
            throws ActivityNotFoundException {

        var optActivity = activityRepository.findById(activityId);
        if (optActivity.isEmpty()) {
            throw new ActivityNotFoundException("Could not find activity by id");
        }
        var activity = optActivity.get();
        activity.setStartTime(newStartTime
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        activity.setEndTime(newEndTime
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        return activityRepository.save(activity);
    }
}
