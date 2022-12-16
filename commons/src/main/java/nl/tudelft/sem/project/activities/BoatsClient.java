package nl.tudelft.sem.project.activities;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.enums.BoatRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(url = "http://localhost:8085/api/boats")
public interface BoatsClient {

    @PostMapping("/add_boat")
    @Headers("Content-Type: application/json")
    BoatDTO addBoat(BoatDTO boatDTO) throws FeignException;

    @GetMapping("/get_boat")
    @Headers("Content-Type: application/json")
    BoatDTO getBoat(UUID boatId) throws FeignException;

    @PostMapping("/rename_boat")
    @Headers("Content-Type: application/json")
    BoatDTO renameBoat(UUID boatId, String newName) throws FeignException;

    @DeleteMapping("/delete_boat")
    @Headers("Content-Type: application/json")
    void deleteBoat(UUID boatId) throws FeignException;

    @PostMapping("/add_position_to_boat")
    @Headers("Content-Type: application/json")
    BoatDTO addPositionToBoat(UUID boatId, BoatRole newPosition) throws FeignException;

    @DeleteMapping("/delete_position_from_boat")
    @Headers("Content-Type: application/json")
    BoatDTO removePositionFromBoat(UUID boatId, BoatRole removedPosition) throws FeignException;
}
