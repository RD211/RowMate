package nl.tudelft.sem.project.activities.controllers;

import nl.tudelft.sem.project.activities.database.entities.Boat;
import nl.tudelft.sem.project.activities.database.repository.BoatRepository;
import nl.tudelft.sem.project.entities.activities.BoatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

/**
 * Controller for Boat mappings.
 */
@RestController
public class BoatController {

    /**
     * The boat repository.
     */
    @Autowired
    transient BoatRepository boatRepository;

    /**
     * A testing mapping to make sure everything works.
     *
     * @return True
     */
    @PostMapping("/test")
    public ResponseEntity<String> testMapping(BoatDTO boatDTO) {

        return ResponseEntity.ok(boatDTO.getName());
    }

    /**
     * The add boat endpoint. There should be no ID in the request added.
     *
     * @param boatDTO The boat DTO, the data will be added to the database.
     * @return The updated DTO of the object, now containing an ID.
     */
    @PostMapping("/add_boat")
    public ResponseEntity<BoatDTO> addBoat(@RequestBody BoatDTO boatDTO) {
        var boat = boatRepository.save(new Boat(boatDTO));
        return ResponseEntity.ok(boat.toDTO());
    }

    /**
     * The get boat endpoint. If a boat exists with a given ID it is returned,
     * otherwise {@link HttpStatus#NOT_FOUND} is returned.
     *
     * @param boatId The id of the requested boat.
     * @return The boat if it exists.
     */
    @GetMapping("/get_boat")
    public ResponseEntity<BoatDTO> getBoat(@RequestParam UUID boatId) {
        Optional<Boat> boat = boatRepository.findById(boatId);
        if (boat.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(boat.get().toDTO());
        }
    }
}
