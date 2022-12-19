package nl.tudelft.sem.project.activities;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.utils.ActivityFilterDTO;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import javax.validation.Valid;
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
}
