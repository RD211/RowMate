package nl.tudelft.sem.project.gateway.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import nl.tudelft.sem.project.activities.*;
import nl.tudelft.sem.project.gateway.CreateCompetitionModel;
import nl.tudelft.sem.project.gateway.CreateTrainingModel;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
import nl.tudelft.sem.project.matchmaking.ActivityApplicationModel;
import nl.tudelft.sem.project.matchmaking.MatchmakingClient;
import nl.tudelft.sem.project.users.UsersClient;
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
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/activities")
public class ActivityController {

    private final transient AuthManager authManager;

    private final transient UsersClient usersClient;

    private final transient ActivitiesClient activitiesClient;

    private final transient BoatsClient boatsClient;

    private final transient MatchmakingClient matchmakingClient;

    /**
     * The activity controller constructor.
     *
     * @param authManager the auth manager.
     * @param usersClient the user client.
     * @param activitiesClient the activities client.
     */
    @Autowired
    public ActivityController(AuthManager authManager, UsersClient usersClient, ActivitiesClient activitiesClient,
                              BoatsClient boatsClient, MatchmakingClient matchmakingClient) {
        this.authManager = authManager;
        this.usersClient = usersClient;
        this.activitiesClient = activitiesClient;
        this.boatsClient = boatsClient;
        this.matchmakingClient = matchmakingClient;
    }


    /**
     * The create training endpoint. Creates a training.
     *
     * @param createTrainingModel the training dto.
     * @return the new training dto.
     */
    @PostMapping("/create_training")
    public ResponseEntity<TrainingDTO> createTraining(@Valid @Validated
                                                          @RequestBody CreateTrainingModel createTrainingModel) {

        if (createTrainingModel.getDateInterval().getStartDate()
                .after(createTrainingModel.getDateInterval().getEndDate())) {
            throw new RuntimeException("Starting time should be before ending time.");
        }

        if (createTrainingModel.getDateInterval().getStartDate().before(Date.from(Instant.now()))) {
            throw new RuntimeException("Starting time should be in the future.");
        }

        var trainingDTO = new TrainingDTO(
                null,
                createTrainingModel.getLocation(),
                authManager.getUsername(),
                createTrainingModel.getDateInterval().getStartDate(),
                createTrainingModel.getDateInterval().getEndDate(),
                createTrainingModel.getBoats()
                        .stream().map(boatsClient::getBoat)
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(activitiesClient.createTraining(trainingDTO));
    }

    /**
     * The create competition endpoint. Creates a competition.
     *
     * @param createCompetitionModel the competition dto.
     * @return the new competition dto.
     */
    @PostMapping("/create_competition")
    public ResponseEntity<CompetitionDTO> createCompetition(@Valid @Validated
                                                      @RequestBody CreateCompetitionModel createCompetitionModel) {
        if (createCompetitionModel.getDateInterval().getStartDate()
                .after(createCompetitionModel.getDateInterval().getEndDate())) {
            throw new RuntimeException("Starting time should be before ending time.");
        }

        if (createCompetitionModel.getDateInterval().getStartDate().before(Date.from(Instant.now()))) {
            throw new RuntimeException("Starting time should be in the future.");
        }


        var competitionDTO = new CompetitionDTO(
                null,
                createCompetitionModel.getLocation(),
                authManager.getUsername(),
                createCompetitionModel.getDateInterval().getStartDate(),
                createCompetitionModel.getDateInterval().getEndDate(),
                createCompetitionModel.getBoats()
                        .stream().map(boatsClient::getBoat)
                        .collect(Collectors.toList()),
                createCompetitionModel.getAllowsAmateurs(),
                createCompetitionModel.getRequiredOrganization(),
                createCompetitionModel.getRequiredGender()
        );

        return ResponseEntity.ok(activitiesClient.createCompetition(competitionDTO));
    }

    /**
     * Gets a competition by id.
     *
     * @param id the id of the competition.
     * @return the competition dto.
     */
    @GetMapping("/get_competition_by_id")
    public ResponseEntity<CompetitionDTO> getCompetitionById(@Valid @RequestParam("id") UUID id) {
        return ResponseEntity.ok(activitiesClient.getCompetition(id));
    }

    /**
     * Gets a training by id.
     *
     * @param id the id of the training.
     * @return the training dto.
     */
    @GetMapping("/get_training_by_id")
    public ResponseEntity<TrainingDTO> getTrainingById(@Valid @RequestParam("id") UUID id) {
        return ResponseEntity.ok(activitiesClient.getTraining(id));
    }

    /**
     * Gets a activity by id.
     *
     * @param id the id of the activity.
     * @return the activity dto.
     */
    @GetMapping("/get_activity_by_id")
    public ResponseEntity<ActivityDTO> getActivityById(@Valid @RequestParam("id") UUID id) {
        return ResponseEntity.ok(activitiesClient.getActivity(id));
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
    public ResponseEntity<ActivityDTO> addBoatToActivity(
            @Valid @NotNull @RequestParam UUID activityId,
            @Valid @NotNull @RequestBody BoatDTO boatDTO
    ) {
        var activity = activitiesClient.getActivity(activityId);
        if (!authManager.getUsername().equals(activity.getOwner())) {
            throw new RuntimeException("You are not the owner of this activity!");
        }
        return ResponseEntity.ok(activitiesClient.addBoatToActivity(activityId, boatDTO));
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
        var activity = activitiesClient.getActivity(changeActivityTimeModel.getActivityId());
        if (!authManager.getUsername().equals(activity.getOwner())) {
            throw new RuntimeException("You are not the owner of this activity!");
        }
        if (changeActivityTimeModel.getNewStartDate().after(changeActivityTimeModel.getNewEndDate())) {
            throw new RuntimeException("Starting time should be before ending time.");
        }
        return ResponseEntity.ok(activitiesClient.changeActivityTimes(changeActivityTimeModel).getBody());
    }

    /**
     * Gets all participants to an activity.
     *
     * @param activityId the activity id.
     * @return the list of participants.
     */
    @GetMapping("/get_participants")
    public ResponseEntity<List<ActivityApplicationModel>> getParticipants(
            @RequestParam @NotNull @Valid UUID activityId) {
        return ResponseEntity.ok(
                matchmakingClient.getApplicationsForActivityByStatus(activityId, true));
    }

    /**
     * Gets all people that are in the waiting room for an activity.
     *
     * @param activityId the activity id.
     * @return the waiting room.
     */
    @GetMapping("/get_waiting_room")
    public ResponseEntity<List<ActivityApplicationModel>> getWaitingRoom(
            @RequestParam @NotNull @Valid UUID activityId) {
        return ResponseEntity.ok(
                matchmakingClient.getApplicationsForActivityByStatus(activityId, false));
    }
}