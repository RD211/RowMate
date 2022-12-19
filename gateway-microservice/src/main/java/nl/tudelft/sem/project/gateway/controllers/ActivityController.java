package nl.tudelft.sem.project.gateway.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import nl.tudelft.sem.project.activities.ActivitiesClient;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
import nl.tudelft.sem.project.users.UsersClient;
import nl.tudelft.sem.project.utils.Fictional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/activities")
public class ActivityController {

    private final transient AuthManager authManager;

    private final transient UsersClient usersClient;

    private final transient ActivitiesClient activitiesClient;

    /**
     * The activity controller constructor.
     *
     * @param authManager the auth manager.
     * @param usersClient the user client.
     * @param activitiesClient the activities client.
     */
    @Autowired
    public ActivityController(AuthManager authManager, UsersClient usersClient, ActivitiesClient activitiesClient) {
        this.authManager = authManager;
        this.usersClient = usersClient;
        this.activitiesClient = activitiesClient;
    }


    /**
     * The create training endpoint. Creates a training.
     *
     * @param trainingDTO the training dto.
     * @return the new training dto.
     */
    @PostMapping("/create_training")
    public ResponseEntity<TrainingDTO> createTraining(@Valid @Validated(Fictional.class)
                                                          @RequestBody TrainingDTO trainingDTO) {
        var username = authManager.getUsername();
        if (!username.equals(trainingDTO.getOwner())) {
            throw new RuntimeException("You can't create an activity for somebody else");
        }

        return ResponseEntity.ok(activitiesClient.createTraining(trainingDTO));
    }

    /**
     * The create competition endpoint. Creates a competition.
     *
     * @param competitionDTO the competition dto.
     * @return the new competition dto.
     */
    @PostMapping("/create_competition")
    public ResponseEntity<CompetitionDTO> createCompetition(@Valid @Validated(Fictional.class)
                                                      @RequestBody CompetitionDTO competitionDTO) {
        var username = authManager.getUsername();
        if (!username.equals(competitionDTO.getOwner())) {
            throw new RuntimeException("You can't create an activity for somebody else");
        }

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