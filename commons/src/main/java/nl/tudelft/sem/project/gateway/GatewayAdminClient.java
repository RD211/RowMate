package nl.tudelft.sem.project.gateway;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(url = "http://localhost:8087/api/admin", name = "gatewayAdminClient")
public interface GatewayAdminClient {
    @DeleteMapping("/delete_user_by_username")
    @Headers("Content-Type: application/json")
    void deleteUserByUsername(@RequestHeader("Authorization") String bearerToken, String username) throws FeignException;

    @DeleteMapping("/delete_user_by_email")
    @Headers("Content-Type: application/json")
    void deleteUserByEmail(@RequestHeader("Authorization") String bearerToken, String email) throws FeignException;

    @PostMapping("/add_boat")
    @Headers("Content-Type: application/json")
    BoatDTO addBoat(@RequestHeader("Authorization") String bearerToken, BoatDTO boatDTO) throws FeignException;

    @PutMapping("/rename_boat?newName={newName}")
    @Headers("Content-Type: application/json")
    BoatDTO renameBoat(@RequestHeader("Authorization") String bearerToken,UUID boatId, @PathVariable(value = "newName") String newName) throws FeignException;

    @DeleteMapping("/delete_boat?boatId={boatId}")
    @Headers("Content-Type: application/json")
    void deleteBoat(@RequestHeader("Authorization") String bearerToken,@PathVariable(value = "boatId") UUID boatId) throws FeignException;

    @PostMapping("/add_position_to_boat?newPosition={newPosition}")
    @Headers("Content-Type: application/json")
    BoatDTO addPositionToBoat(@RequestHeader("Authorization") String bearerToken,UUID boatId, @PathVariable(value = "newPosition") BoatRole newPosition) throws FeignException;

    @DeleteMapping("/delete_position_from_boat?removePosition={removedPosition}")
    @Headers("Content-Type: application/json")
    BoatDTO removePositionFromBoat(@RequestHeader("Authorization") String bearerToken,UUID boatId, @PathVariable(value = "removedPosition") BoatRole removedPosition) throws FeignException;

    @PutMapping("/change_cox_certificate?boatId={boatId}&newCertificateId={newCertificateId}")
    @Headers("Content-Type: application/json")
    BoatDTO changeCoxCertificate(@RequestHeader("Authorization") String bearerToken,@PathVariable(value = "boatId") UUID boatId, @PathVariable(value = "newCertificateId") UUID newCertificateId);
}