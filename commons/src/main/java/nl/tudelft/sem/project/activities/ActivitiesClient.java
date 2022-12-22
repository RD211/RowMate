package nl.tudelft.sem.project.activities;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name="activities-microservice", url="http://localhost:8085/api/activity")
public interface ActivitiesClient {

    @PostMapping(value="/find")
    @Headers("Content-Type: application/json")
    List<ActivityDTO> findActivitiesFromFilter(@RequestBody ActivityFilterDTO dto) throws FeignException;

    @PostMapping(value="/create_training")
    @Headers("Content-Type: application/json")
    TrainingDTO createTraining(@RequestBody TrainingDTO dto) throws FeignException;

    @PostMapping(value="/create_competition")
    @Headers("Content-Type: application/json")
    CompetitionDTO createCompetition(@RequestBody CompetitionDTO dto) throws FeignException;

    @GetMapping(value="/get_competition_by_id?id={id}")
    @Headers("Content-Type: application/json")
    CompetitionDTO getCompetition(@PathVariable(value = "id") UUID id) throws FeignException;

    @GetMapping(value="/get_training_by_id?id={id}")
    @Headers("Content-Type: application/json")
    TrainingDTO getTraining(@PathVariable(value = "id") UUID id) throws FeignException;

    @GetMapping(value="/get_activity_by_id?id={id}")
    @Headers("Content-Type: application/json")
    ActivityDTO getActivity(@PathVariable(value = "id") UUID id) throws FeignException;

    @PostMapping(value="/add_boat_to_activity?activityId={activityId}")
    @Headers("Content-Type: application/json")
    ActivityDTO addBoatToActivity(@PathVariable(value = "activityId") UUID activityId,
                                  @RequestBody BoatDTO boatDTO) throws FeignException;

    @PutMapping(value="/change_activity_times")
    @Headers("Content-Type: application/json")
    ResponseEntity<ActivityDTO> changeActivityTimes(
            @RequestBody ChangeActivityTimeModel changeActivityTimeModel
    ) throws FeignException;
}
