package nl.tudelft.sem.project.gateway;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name="gatewayActivitiesClient", url="http://localhost:8087/api/activities")

public interface GatewayActivitiesClient {
    @PostMapping(value="/create_training")
    @Headers("Content-Type: application/json")
    TrainingDTO createTraining(@RequestHeader("Authorization") String bearerToken, @RequestBody TrainingDTO dto) throws FeignException;

    @PostMapping(value="/create_competition")
    @Headers("Content-Type: application/json")
    CompetitionDTO createCompetition(@RequestHeader("Authorization") String bearerToken, @RequestBody CompetitionDTO dto) throws FeignException;

    @GetMapping(value="/get_competition_by_id?id={id}")
    @Headers("Content-Type: application/json")
    CompetitionDTO getCompetition(@RequestHeader("Authorization") String bearerToken, @PathVariable(value = "id") UUID id) throws FeignException;

    @GetMapping(value="/get_training_by_id?id={id}")
    @Headers("Content-Type: application/json")
    TrainingDTO getTraining(@RequestHeader("Authorization") String bearerToken,@PathVariable(value = "id") UUID id) throws FeignException;

    @GetMapping(value="/get_activity_by_id?id={id}")
    @Headers("Content-Type: application/json")
    ActivityDTO getActivity(@RequestHeader("Authorization") String bearerToken, @PathVariable(value = "id") UUID id) throws FeignException;
}
