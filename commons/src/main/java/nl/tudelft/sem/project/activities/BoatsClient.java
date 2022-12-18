package nl.tudelft.sem.project.activities;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.enums.BoatRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(url = "http://localhost:8085/api/boats", name = "boatsClient")
public interface BoatsClient {

    @PostMapping("/add_boat")
    @Headers("Content-Type: application/json")
    BoatDTO addBoat(BoatDTO boatDTO) throws FeignException;

    @GetMapping("/get_boat?boatId={boatId}")
    @Headers("Content-Type: application/json")
    BoatDTO getBoat(@PathVariable(value = "boatId") UUID boatId) throws FeignException;

    @GetMapping("/get_boats")
    @Headers("Content-Type: application/json")
    List<BoatDTO> getAllBoats() throws FeignException;

    @PutMapping("/rename_boat?newName={newName}")
    @Headers("Content-Type: application/json")
    BoatDTO renameBoat(UUID boatId, @PathVariable(value = "newName") String newName) throws FeignException;

    @DeleteMapping("/delete_boat?boatId={boatId}")
    @Headers("Content-Type: application/json")
    void deleteBoat(@PathVariable(value = "boatId") UUID boatId) throws FeignException;

    @PostMapping("/add_position_to_boat?newPosition={newPosition}")
    @Headers("Content-Type: application/json")
    BoatDTO addPositionToBoat(UUID boatId, @PathVariable(value = "newPosition") BoatRole newPosition) throws FeignException;

    @DeleteMapping("/remove_position_from_boat?removedPosition={removedPosition}")
    @Headers("Content-Type: application/json")
    BoatDTO removePositionFromBoat(UUID boatId, @PathVariable(value = "removedPosition") BoatRole removedPosition) throws FeignException;

    @PutMapping("/change_cox_certificate?boatId={boatId}&newCertificateId={newCertificateId}")
    @Headers("Content-Type: application/json")
    BoatDTO changeCoxCertificate(@PathVariable(value = "boatId") UUID boatId, @PathVariable(value = "newCertificateId") UUID newCertificateId);
}
