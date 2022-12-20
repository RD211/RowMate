package nl.tudelft.sem.project.activities.controllers;

import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.activities.config.AppConfig;
import nl.tudelft.sem.project.activities.database.entities.ActivityConverterService;
import nl.tudelft.sem.project.activities.database.entities.CompetitionConverterService;
import nl.tudelft.sem.project.activities.database.entities.TrainingConverterService;
import nl.tudelft.sem.project.activities.services.ActivityService;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;
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
}
