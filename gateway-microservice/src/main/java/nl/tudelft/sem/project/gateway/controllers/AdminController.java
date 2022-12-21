package nl.tudelft.sem.project.gateway.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.BoatsClient;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.*;
import nl.tudelft.sem.project.users.models.ChangeCertificateNameModel;
import nl.tudelft.sem.project.users.models.ChangeCertificateSupersededModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private transient UsersClient usersClient;
    @Autowired
    private transient BoatsClient boatsClient;
    @Autowired
    private transient CertificatesClient certificatesClient;

    /**
     * Deletes a user by username.
     * This is an admin endpoint.
     *
     * @param username the username of the user to delete
     * @return nothing
     */
    @DeleteMapping("/delete_user_by_username")
    public ResponseEntity<Void> deleteUserByUsername(@Valid @RequestBody @NotNull String username) {
        usersClient.deleteUserByUsername(
                new Username(username)
        );
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a user by email.
     * This is an admin endpoint.
     *
     * @param email the email of the user to delete
     * @return nothing
     */
    @DeleteMapping("/delete_user_by_email")
    public ResponseEntity<Void> deleteUserByEmail(@Valid @RequestBody @NotNull String email) {
        usersClient.deleteUserByEmail(
                new UserEmail(email)
        );
        return ResponseEntity.ok().build();
    }


    /**
     * Adds a boat to the database.
     * The cox certificate must also be valid.
     *
     * @param boat the boat to add.
     * @return the added boat dto.
     */
    @PostMapping("/add_boat")
    public ResponseEntity<BoatDTO> addBoat(@Valid @RequestBody @NotNull BoatDTO boat) {

        // This checks that we actually have a certificate with this id.
        certificatesClient.getCertificateById(boat.getCoxCertificateId());
        var boatDTO = boatsClient.addBoat(boat);
        return ResponseEntity.ok(boatDTO);
    }

    /**
     * Rename a boat from the database.
     *
     * @param boatId the boat id.
     * @param newName the new boat name.
     * @return the new boat dto.
     */
    @PutMapping("/rename_boat")
    public ResponseEntity<BoatDTO> renameBoat(@Valid @RequestBody @NotNull UUID boatId,
                                              @Valid @NotNull @RequestParam("newName") String newName) {
        var boatDTO = boatsClient.renameBoat(boatId, newName);
        return ResponseEntity.ok(boatDTO);
    }

    /**
     * The delete a boat endpoint.
     *
     * @param boatId the boat id.
     * @return nothing.
     */
    @DeleteMapping("/delete_boat")
    public ResponseEntity<Void> deleteBoat(@Valid @RequestParam("boatId") @NotNull UUID boatId) {
        boatsClient.deleteBoat(boatId);
        return ResponseEntity.ok().build();
    }

    /**
     * Adds a position to a boat.
     *
     * @param boatId the boat id.
     * @param newPosition the new position.
     * @return the new boat dto.
     */
    @PostMapping("/add_position_to_boat")
    public ResponseEntity<BoatDTO> addPositionToBoat(@Valid @RequestBody @NotNull UUID boatId,
                                                     @Valid @NotNull
                                                     @RequestParam("newPosition") BoatRole newPosition) {
        var boatDTO = boatsClient.addPositionToBoat(boatId, newPosition);
        return ResponseEntity.ok(boatDTO);
    }

    /**
     * Deletes a position from a boat.
     *
     * @param boatId the boat id.
     * @param removedPosition the position to remove.
     * @return the new boat dto.
     */
    @DeleteMapping("/delete_position_from_boat")
    public ResponseEntity<BoatDTO> removePositionFromBoat(@Valid @RequestBody @NotNull UUID boatId,
                                                     @Valid @NotNull
                                                     @RequestParam("removePosition") BoatRole removedPosition) {
        var boatDTO = boatsClient.removePositionFromBoat(boatId, removedPosition);
        return ResponseEntity.ok(boatDTO);
    }

    /**
     * The change cox certificate endpoint.
     * This will change the certifiate of a boat to a different one.
     *
     * @param boatId the boat id.
     * @param newCertificate the new certificate id.
     * @return the new boat dto.
     */
    @PutMapping("/change_cox_certificate")
    public ResponseEntity<BoatDTO> changeCoxCertificate(@Valid @RequestParam("boatId") @NotNull UUID boatId,
                                                          @Valid @NotNull
                                                          @RequestParam("newCertificateId") UUID newCertificate) {
        // This checks that we actually have a certificate with this id.
        certificatesClient.getCertificateById(newCertificate);
        var boatDTO = boatsClient.changeCoxCertificate(boatId, newCertificate);
        return ResponseEntity.ok(boatDTO);
    }

    /**
     * Endpoint to update the certificate name.
     *
     * @param changeCertificateNameModel The model that specifies the certificate and its new name.
     * @return The updated certificate.
     */
    @PutMapping("/change_certificate_name")
    public ResponseEntity<CertificateDTO> changeCertificateName(
            @Valid @NotNull @RequestBody ChangeCertificateNameModel changeCertificateNameModel) {
        var changed = certificatesClient.changeCertificateName(changeCertificateNameModel);
        return ResponseEntity.ok(changed);
    }

    /**
     * Endpoint to update the certificate's superseded certificate.
     *
     * @param changeCertificateSupersededModel The model that specifies the certificate and its new superseded one.
     * @return The updated certificate.
     */
    @PutMapping("/change_certificate_superseded")
    public ResponseEntity<CertificateDTO> changeCertificateSuperseded(
            @Valid @NotNull @RequestBody ChangeCertificateSupersededModel changeCertificateSupersededModel) {
        var changed = certificatesClient.changeCertificateSuperseded(changeCertificateSupersededModel);
        return ResponseEntity.ok(changed);
    }

    /**
     * Endpoint to add new certificates to the system.
     *
     * @param certificateDTO The new certificate to be added to the system.
     * @return The certificate that was added along with its new id.
     */
    @PostMapping("/add_certificate")
    public ResponseEntity<CertificateDTO> addCertificate(
            @Valid @Validated @RequestBody CertificateDTO certificateDTO) {
        var saved = certificatesClient.addCertificate(certificateDTO);
        return ResponseEntity.ok(saved);
    }
}
