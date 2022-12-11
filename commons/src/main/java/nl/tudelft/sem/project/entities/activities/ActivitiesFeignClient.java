package nl.tudelft.sem.project.entities.activities;

import nl.tudelft.sem.project.entities.utils.ActivityFilterDTO;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="activities-microservice", url="http://localhost:8085")
public interface ActivitiesFeignClient {

    @PostMapping(value="/activity/find")
    List<ActivityDTO> findActivitiesFromFilter(@RequestBody ActivityFilterDTO dto);
}
