package nl.tudelft.sem.project.activities.controllers;

import feign.FeignException;
import nl.tudelft.sem.project.activities.*;
import nl.tudelft.sem.project.activities.database.entities.*;
import nl.tudelft.sem.project.activities.services.ActivityService;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/activity")
@Validated
public class ActivityController {

    @Autowired
    transient ActivityService activityService;
    @Autowired
    transient TrainingConverterService trainingConverterService;
    @Autowired
    transient CompetitionConverterService competitionConverterService;
    @Autowired
    transient ActivityConverterService activityConverterService;
    @Autowired
    transient BoatsClient boatsClient;
    @Autowired
    transient BoatConverterService boatConverterService;

    /**
     * The find endpoint. Finds an activity given the filter.
     *
     * @param dto the filter dto.
     * @return a list of activities.
     */
    @PostMapping("/find")
    public ResponseEntity<List<ActivityDTO>> findActivitiesFromFilter(
            @Valid @NotNull @RequestBody ActivityFilterDTO dto) {
        var foundActivities = activityService.findActivitiesFromFilter(dto)
                .stream().map(activityConverterService::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(foundActivities);
    }

    /**
     * Creates a training.
     *
     * @param dto the training dto.
     * @return the new training dto.
     */
    @PostMapping("/create_training")
    public ResponseEntity<TrainingDTO> createTraining(@Valid @NotNull @RequestBody TrainingDTO dto) {
        var training = trainingConverterService.toEntity(dto);
        var trainingDTO =
                trainingConverterService.toDTO(activityService.addTraining(training));

        return ResponseEntity.ok(trainingDTO);
    }

    /**
     * Creates a competition.
     *
     * @param dto the competition dto.
     * @return the new competition dto.
     */
    @PostMapping("/create_competition")
    public ResponseEntity<CompetitionDTO> createCompetition(@Valid @NotNull @RequestBody CompetitionDTO dto) {
        var competition = competitionConverterService.toEntity(dto);
        var competitionDTO =
                competitionConverterService.toDTO(activityService.addCompetition(competition));

        return ResponseEntity.ok(competitionDTO);
    }

    /**
     * Gets a competition by id.
     *
     * @param id the id of the competition.
     * @return the competition.
     */
    @GetMapping("/get_competition_by_id")
    public ResponseEntity<CompetitionDTO> getCompetitionById(@Valid @NotNull @RequestParam UUID id) {
        return ResponseEntity.ok(
                competitionConverterService.toDTO(activityService.getCompetitionById(id)));
    }

    /**
     * Gets a training by id.
     *
     * @param id the id of the training.
     * @return the training.
     */
    @GetMapping("/get_training_by_id")
    public ResponseEntity<TrainingDTO> getTrainingById(@Valid @NotNull @RequestParam UUID id) {
        return ResponseEntity.ok(
                trainingConverterService.toDTO(activityService.getTrainingById(id)));
    }

    /**
     * Gets a activity by id.
     *
     * @param id the id of the activity.
     * @return the activity.
     */
    @GetMapping("/get_activity_by_id")
    public ResponseEntity<ActivityDTO> getActivityById(@Valid @NotNull @RequestParam UUID id) {
        return ResponseEntity.ok(
                activityConverterService.toDTO(activityService.getActivityById(id)));
    }

    /**
     * Adds a new boat to a given activity. If the given boat is not one
     * in the database it is also added on top of that.
     *
     * @param activityId ID of the activity.
     * @param boatDTO Boat DTO object to add.
     * @return The activity object after it has changed.
     */
    @PostMapping("/add_boat_to_activity")
    @SuppressWarnings("PMD")
    public ResponseEntity<ActivityDTO> addBoatToActivity(
            @Valid @NotNull @RequestParam UUID activityId,
            @Valid @NotNull @RequestBody BoatDTO boatDTO
    ) {
        BoatDTO boat;
        try {
            boat = boatsClient.getBoat(boatDTO.getBoatId());
        } catch (FeignException e) {
            boat = boatsClient.addBoat(boatDTO);
        }
        var activity = activityService.addBoatToActivity(activityId, boatConverterService.toEntity(boat));
        return ResponseEntity.ok(activityConverterService.toDTO(activity));
    }

    /**
     * Changes the times of the activity.
     *
     * @param changeActivityTimeModel Model containing activity id and the new times.
     * @return ActivityDTO of the changed activity. Returns 404 if there is no such
     *         activity.
     */
    @PutMapping("/change_activity_times")
    public ResponseEntity<ActivityDTO> changeActivityTimes(
            @Valid @NotNull @RequestBody ChangeActivityTimeModel changeActivityTimeModel
    ) {
        var activity = activityService.changeActivityTimes(
                changeActivityTimeModel.getActivityId(),
                changeActivityTimeModel.getNewStartDate(),
                changeActivityTimeModel.getNewEndDate()
        );
        return ResponseEntity.ok(activityConverterService.toDTO(activity));
    }
}
