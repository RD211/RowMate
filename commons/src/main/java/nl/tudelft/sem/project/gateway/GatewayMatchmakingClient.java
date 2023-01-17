package nl.tudelft.sem.project.gateway;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import nl.tudelft.sem.project.matchmaking.ActivityRegistrationResponseDTO;
import nl.tudelft.sem.project.matchmaking.UserActivityApplication;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name="gatewayMatchmakingClient", url="http://localhost:8087/api/matchmaking")

public interface GatewayMatchmakingClient {
    @PostMapping(value="/list")
    @Headers("Content-Type: application/json")
    List<ActivityDTO> findActivities(@RequestHeader("Authorization") String bearerToken,
                                     @RequestBody ActivityFilterDTO activityFilterDTO) throws FeignException;

    @PostMapping(value="/find/{strategy}")
    @Headers("Content-Type: application/json")
    String autoFindActivity(@RequestHeader("Authorization") String bearerToken,
                            @PathVariable(value = "strategy") MatchmakingStrategy strategy,
                            @RequestBody ActivityFilterDTO activityFilterDTO) throws FeignException;
}
