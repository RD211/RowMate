package nl.tudelft.sem.project.activities.controllers;

import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.database.entities.Boat;
import nl.tudelft.sem.project.activities.database.entities.BoatService;
import nl.tudelft.sem.project.activities.database.repository.BoatRepository;
import nl.tudelft.sem.project.activities.exceptions.BoatNotFoundException;
import nl.tudelft.sem.project.activities.exceptions.RoleNotFoundException;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.utils.Fictional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Controller for Boat mappings.
 */
@RestController
@RequestMapping("/api/boats")
public class BoatController {

    /**
     * The boat repository.
     */
    @Autowired
    transient BoatRepository boatRepository;

    /**
     * The boat service.
     */
    @Autowired
    transient BoatService boatService;

    /**
     * The add boat endpoint. There should be no ID in the request added.
     *
     * @param boatDTO The boat DTO, the data will be added to the database.
     * @return The updated DTO of the object, now containing an ID.
     */
    @PostMapping("/add_boat")
    public ResponseEntity<BoatDTO> addBoat(
            @Valid @Validated(Fictional.class) @RequestBody BoatDTO boatDTO
    ) {
        Boat boat = boatService.addBoat(new Boat(boatDTO));
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
    public ResponseEntity<BoatDTO> getBoat(
            @Valid @NotNull @RequestParam UUID boatId
    ) {
        try {
            Boat boat = boatService.getBoatById(boatId);
            return ResponseEntity.ok(boat.toDTO());
        } catch (BoatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Renames a boat in the repository.
     *
     * @param boatId The ID of the boat to rename.
     * @param newName The new name for the boat.
     * @return A boat DTO object if the boat was successfully renamed. If there
     *         was no such boat, then {@link HttpStatus#NOT_FOUND} is returned.
     */
    @PostMapping("/rename_boat")
    public ResponseEntity<BoatDTO> renameBoat(
            @Valid @NotNull @RequestBody UUID boatId,
            @Valid @NotNull @RequestParam String newName
    ) {
        try {
            Boat boat = boatService.renameBoat(boatId, newName);
            return ResponseEntity.ok(boat.toDTO());
        } catch (BoatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Deletes a boat by a given ID.
     *
     * @param boatId The ID of the boat.
     * @return {@link HttpStatus#OK} status if the boat was successfully removed.
     *         If there was no such boat, then {@link HttpStatus#NOT_FOUND} is returned.
     */
    @DeleteMapping("/delete_boat")
    public ResponseEntity<Void> deleteBoat(
            @Valid @NotNull @RequestParam UUID boatId
    ) {
        try {
            boatService.deleteBoatById(boatId);
            return ResponseEntity.ok().build();
        } catch (BoatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Adds a new available position to the boat.
     *
     * @param boatId The id of the boat.
     * @param newPosition The new position to add.
     * @return A boat DTO object if a position was successfully added. If there
     *         was no such boat, then {@link HttpStatus#NOT_FOUND} is returned.
     */
    @PostMapping("/add_position_to_boat")
    public ResponseEntity<BoatDTO> addPositionToBoat(
            @Valid @NotNull @RequestBody UUID boatId,
            @Valid @NotNull @RequestParam BoatRole newPosition
    ) {
        try {
            Boat boat = boatService.addAvailablePositionToBoat(boatId, newPosition);
            return ResponseEntity.ok(boat.toDTO());
        } catch (BoatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Removes an available position from the boat.
     *
     * @param boatId The id of the boat.
     * @param removedPosition The position to remove.
     * @return A baot DTO object if a position was successfully removed. If there
     *         was no such boat, then {@link HttpStatus#NOT_FOUND} is returned. If there
     *         was no such position, then {@link HttpStatus#CONFLICT} is returned.
     */
    @DeleteMapping("/remove_position_from_boat")
    public ResponseEntity<BoatDTO> removePositionFromBoat(
            @Valid @NotNull @RequestBody UUID boatId,
            @Valid @NotNull @RequestParam BoatRole removedPosition
    ) {
        try {
            Boat boat = boatService.removeAvailablePositionFromBoat(boatId, removedPosition);
            return ResponseEntity.ok(boat.toDTO());
        } catch (BoatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
