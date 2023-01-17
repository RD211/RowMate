package nl.tudelft.sem.project.gateway;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.matchmaking.ActivityApplicationModel;
import nl.tudelft.sem.project.matchmaking.ActivityRegistrationResponseDTO;
import nl.tudelft.sem.project.matchmaking.UserActivityApplication;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(url = "http://localhost:8087/api/registrations", name = "gatewayActivityRegistrationClient")
public interface GatewayActivityRegistrationClient {
    @PostMapping(value="/register")
    @Headers("Content-Type: application/json")
    String registerInActivity(@RequestHeader("Authorization") String bearerToken,
                              @RequestBody SeatedUserModel seatedUserModel) throws FeignException;

    @PostMapping(value="/deregister")
    @Headers("Content-Type: application/json")
    String deRegisterFromActivity(@RequestHeader("Authorization") String bearerToken,
                                  @RequestBody UUID activityId) throws FeignException;


    @PostMapping(value="/respond")
    @Headers("Content-Type: application/json")
    String respondToRegistration(@RequestHeader("Authorization") String bearerToken,
                                 @RequestBody ActivityRegistrationResponseDTO activityRegistrationResponseDTO) throws FeignException;

    @GetMapping(value="/get_waiting_applications")
    @Headers("Content-Type: application/json")
    List<UserActivityApplication> getWaitingApplications(@RequestHeader("Authorization") String bearerToken);

    @GetMapping(value="/get_accepted_applications")
    @Headers("Content-Type: application/json")
    List<UserActivityApplication> getAcceptedApplications(@RequestHeader("Authorization") String bearerToken);

    @DeleteMapping("/delete_user_from_activity?activityId={activityId}&userName={userName}")
    @Headers("Content-Type: application/json")
    ResponseEntity<Void> deleteByUserNameAndActivityId(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable(value="activityId") UUID activityId,
            @PathVariable(value="userName") String userName
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
