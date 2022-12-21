package nl.tudelft.sem.project.gateway.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import nl.tudelft.sem.project.activities.*;
import nl.tudelft.sem.project.gateway.CreateCompetitionModel;
import nl.tudelft.sem.project.gateway.CreateTrainingModel;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.NotificationsClient;
import nl.tudelft.sem.project.notifications.NotificationDTO;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/activities")
public class ActivityController {

    private final transient AuthManager authManager;

    private final transient UsersClient usersClient;

    private final transient ActivitiesClient activitiesClient;

    private final transient NotificationsClient notificationsClient;

    private final transient BoatsClient boatsClient;

    /**
     * The activity controller constructor.
     *
     * @param authManager      the auth manager.
     * @param usersClient      the user client.
     * @param activitiesClient the activities client.
     */
    @Autowired
    public ActivityController(AuthManager authManager, UsersClient usersClient, ActivitiesClient activitiesClient,
                              BoatsClient boatsClient, NotificationsClient notificationsClient) {
        this.authManager = authManager;
        this.usersClient = usersClient;
        this.activitiesClient = activitiesClient;
        this.notificationsClient = notificationsClient;
        this.boatsClient = boatsClient;
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

        UserDTO userDTO = usersClient.getUserByUsername(new Username(authManager.getUsername()));
        // Send notification that the training has been created.
        notificationsClient.sendNotification(NotificationDTO.builder()
                .userDTO(userDTO)
                .activityDTO(trainingDTO)
                .eventType(EventType.CREATED_ACTIVITY)
                .build());

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

        UserDTO userDTO = usersClient.getUserByUsername(new Username(authManager.getUsername()));
        // Send notification that competition has been created.
        notificationsClient.sendNotification(NotificationDTO.builder()
                .userDTO(userDTO)
                .activityDTO(competitionDTO)
                .eventType(EventType.CREATED_ACTIVITY)
                .build());

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
}