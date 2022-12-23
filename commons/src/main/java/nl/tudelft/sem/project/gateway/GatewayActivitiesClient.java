package nl.tudelft.sem.project.gateway;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.activities.*;
import nl.tudelft.sem.project.matchmaking.ActivityApplicationModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name="gatewayActivitiesClient", url="http://localhost:8087/api/activities")

public interface GatewayActivitiesClient {
    @PostMapping(value="/create_training")
    @Headers("Content-Type: application/json")
    TrainingDTO createTraining(@RequestHeader("Authorization") String bearerToken, @RequestBody CreateTrainingModel dto) throws FeignException;

    @PostMapping(value="/create_competition")
    @Headers("Content-Type: application/json")
    CompetitionDTO createCompetition(@RequestHeader("Authorization") String bearerToken, @RequestBody CreateCompetitionModel dto) throws FeignException;

    @GetMapping(value="/get_competition_by_id?id={id}")
    @Headers("Content-Type: application/json")
    CompetitionDTO getCompetition(@RequestHeader("Authorization") String bearerToken, @PathVariable(value = "id") UUID id) throws FeignException;

    @GetMapping(value="/get_training_by_id?id={id}")
    @Headers("Content-Type: application/json")
    TrainingDTO getTraining(@RequestHeader("Authorization") String bearerToken,@PathVariable(value = "id") UUID id) throws FeignException;

    @GetMapping(value="/get_activity_by_id?id={id}")
    @Headers("Content-Type: application/json")
    ActivityDTO getActivity(@RequestHeader("Authorization") String bearerToken, @PathVariable(value = "id") UUID id) throws FeignException;

    @PostMapping("/add_boat_to_activity?activityId={activityId}")
    @Headers("Content-Type: application/json")
    ResponseEntity<ActivityDTO> addBoatToActivity(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable(value="activityId") UUID activityId,
            @RequestBody BoatDTO boatDTO
    ) throws FeignException;

    @PutMapping("/change_activity_times")
    @Headers("Content-Type: application/json")
    ResponseEntity<ActivityDTO> changeActivityTimes(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody ChangeActivityTimeModel changeActivityTimeModel
    ) throws FeignException;

    @GetMapping("/get_participants?activityId={activityId}")
    @Headers("Content-Type: application/json")
    List<ActivityApplicationModel> getParticipants(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable(value = "activityId") UUID activityId
    );

    @GetMapping("/get_waiting_room?activityId={activityId}")
    @Headers("Content-Type: application/json")
    List<ActivityApplicationModel> getWaitingRoom(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable(value = "activityId") UUID activityId
    );
}
