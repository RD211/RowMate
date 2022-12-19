package nl.tudelft.sem.project.gateway;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.activities.BoatDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.UUID;

@FeignClient(url = "http://localhost:8087/api/boats", name = "gatewayBoatsClient")
public interface GatewayBoatsClient {
    @GetMapping("/get_boat?boatId={boatId}")
    @Headers("Content-Type: application/json")
    BoatDTO getBoat(@RequestHeader("Authorization") String bearerToken, @PathVariable(value = "boatId") UUID boatId) throws FeignException;

    @GetMapping("/get_boats")
    @Headers("Content-Type: application/json")
    List<BoatDTO> getAllBoats(@RequestHeader("Authorization") String bearerToken) throws FeignException;
}
