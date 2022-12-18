package nl.tudelft.sem.project.entities.activities;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.entities.utils.ActivityFilterDTO;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;
import java.util.UUID;

@FeignClient(name="activities-microservice", url="http://localhost:8085")
public interface ActivitiesFeignClient {

    @PostMapping(value="/activity/find")
    @Headers("Content-Type: application/json")
    List<ActivityDTO> findActivitiesFromFilter(@RequestBody ActivityFilterDTO dto) throws FeignException;

    @PostMapping(value = "/get_boat/{boatId}")
    @Headers("Content-Type: application/json")
    BoatDTO getBoatByUUID(@PathVariable("boatId") UUID boatId) throws FeignException;
}
