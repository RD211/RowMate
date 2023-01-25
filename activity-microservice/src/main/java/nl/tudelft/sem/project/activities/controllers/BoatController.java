package nl.tudelft.sem.project.activities.controllers;

import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.database.entities.Boat;
import nl.tudelft.sem.project.activities.database.entities.BoatConverterService;
import nl.tudelft.sem.project.activities.database.entities.BoatService;
import nl.tudelft.sem.project.activities.database.repository.BoatRepository;
import nl.tudelft.sem.project.enums.BoatRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    transient BoatConverterService boatConverterService;

    /**
     * The add boat endpoint. There should be no ID in the request added.
     *
     * @param boatDTO The boat DTO, the data will be added to the database.
     * @return The updated DTO of the object, now containing an ID.
     */
    @PostMapping("/add_boat")
    public ResponseEntity<BoatDTO> addBoat(
            @Valid @RequestBody BoatDTO boatDTO
    ) {
        Boat boat = boatService.addBoat(boatConverterService.toEntity(boatDTO));
        return ResponseEntity.ok(boatConverterService.toDTO(boat));
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
        Boat boat = boatService.getBoatById(boatId);
        return ResponseEntity.ok(boatConverterService.toDTO(boat));
    }

    /**
     * The get all boats endpoint.
     * This endpoint gets all the boats.
     *
     * @return the list of boats.
     */
    @GetMapping("/get_boats")
    public ResponseEntity<List<BoatDTO>> getBoats() {
        var boats = boatService.getAllBoats().stream()
                .map(boatConverterService::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(boats);
    }

    /**
     * Renames a boat in the repository.
     *
     * @param boatId The ID of the boat to rename.
     * @param newName The new name for the boat.
     * @return A boat DTO object if the boat was successfully renamed. If there
     *         was no such boat, then {@link HttpStatus#NOT_FOUND} is returned.
     */
    @PutMapping("/rename_boat")
    public ResponseEntity<BoatDTO> renameBoat(
            @Valid @NotNull @RequestBody UUID boatId,
            @Valid @NotNull @RequestParam String newName
    ) {
        Boat boat = boatService.renameBoat(boatId, newName);
        return ResponseEntity.ok(boatConverterService.toDTO(boat));
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
        boatService.deleteBoatById(boatId);
        return ResponseEntity.ok().build();
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
        Boat boat = boatService.addAvailablePositionToBoat(boatId, newPosition);
        return ResponseEntity.ok(boatConverterService.toDTO(boat));
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
        Boat boat = boatService.removeAvailablePositionFromBoat(boatId, removedPosition);
        return ResponseEntity.ok(boatConverterService.toDTO(boat));
    }

    /**
     * Changes the certificate required by the cox for the boat.
     *
     * @param boatId The id of the boat.
     * @param newCertificateId The id of the new certificate.
     * @return A boat DTO object with the updated Boat upon success. If either
     *         the boatId or certificateId is not found, then {@link HttpStatus#NOT_FOUND}.
     */
    @PutMapping("/change_cox_certificate")
    public ResponseEntity<BoatDTO> changeCoxCertificate(
            @Valid @NotNull @RequestParam UUID boatId,
            @Valid @NotNull @RequestParam UUID newCertificateId
    ) {
        Boat boat = boatService.changeCoxCertificate(boatId, newCertificateId);
        return ResponseEntity.ok(boatConverterService.toDTO(boat));
    }

}
