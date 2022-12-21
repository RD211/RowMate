package nl.tudelft.sem.project.matchmaking;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name="matchmakingClient", url="http://localhost:8083/api/matchmaking")
public interface MatchmakingClient {
    @PostMapping(value="/list")
    @Headers("Content-Type: application/json")
    List<ActivityDTO> findActivities(@RequestBody ActivityRequestDTO dto) throws FeignException;

    @PostMapping(value="/find/{strategy}")
    @Headers("Content-Type: application/json")
    String autoFindActivity(@PathVariable(value = "strategy") MatchmakingStrategy strategy,
                                     @RequestBody ActivityRequestDTO dto) throws FeignException;

    @PostMapping(value="register")
    @Headers("Content-Type: application/json")
    String registerInActivity(@RequestBody ActivityRegistrationRequestDTO dto) throws FeignException;

    @PostMapping(value="deregister")
    @Headers("Content-Type: application/json")
    String deRegisterFromActivity(@RequestBody ActivityDeregisterRequestDTO dto) throws FeignException;

    @GetMapping(value="/get_waiting_applications?username={username}")
    @Headers("Content-Type: application/json")
    List<UserActivityApplication> getWaitingApplications(@PathVariable("username") String username);

    @GetMapping(value="/get_accepted_applications?username={username}")
    @Headers("Content-Type: application/json")
    List<UserActivityApplication> getAcceptedApplications(@PathVariable("username") String username);

    @DeleteMapping("/delete_user_from_activity?activityId={activityId}&userName={userName}")
    @Headers("Content-Type: application/json")
    ResponseEntity<Void> deleteByUserNameAndActivityId(
            @PathVariable(value = "activityId") UUID activityId,
            @PathVariable(value = "userName") String userName
    );
}
