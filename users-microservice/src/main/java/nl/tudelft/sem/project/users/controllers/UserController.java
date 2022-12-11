package nl.tudelft.sem.project.users.controllers;

import lombok.NonNull;
import nl.tudelft.sem.project.DateInterval;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.database.repositories.UserRepository;
import nl.tudelft.sem.project.users.domain.certificate.Certificate;
import nl.tudelft.sem.project.users.domain.certificate.CertificateNotFoundException;
import nl.tudelft.sem.project.users.domain.users.*;
import nl.tudelft.sem.project.users.models.AddAvailabilityModel;
import nl.tudelft.sem.project.users.models.AddCertificateUserModel;
import nl.tudelft.sem.project.users.models.ChangeGenderModel;
import nl.tudelft.sem.project.users.models.RemoveAvailabilityModel;
import nl.tudelft.sem.project.utils.Existing;
import nl.tudelft.sem.project.utils.Fictional;
import nl.tudelft.sem.project.utils.FutureDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    transient UserRepository userRepository;

    @Autowired
    transient UserService userService;
    @Autowired
    transient UserConverterService userConverterService;


    /**
     * The add user endpoint. It adds a user to the database given the UserDTO.
     *
     * @param userDTO the user dto, the data that will be added to the database.
     * @return the updated dto including the ID.
     */
    @PostMapping("/add_user")
    public ResponseEntity<UserDTO> addUser(@Valid @Validated(Fictional.class) @RequestBody UserDTO userDTO) {
        var user = userConverterService.toEntity(userDTO);
        var savedUser = userService.addUser(user);
        return ResponseEntity.ok(userConverterService.toDTO(savedUser));
    }


    /**
     * Gets a userDTO by the id.
     *
     * @param userId the requested id.
     * @return the userDTO.
     */
    @GetMapping("/get_user_by_id")
    public ResponseEntity<UserDTO> getUserById(@Valid @NotNull @RequestParam  UUID userId) {
        var user = userService.getUserById(userId);
        return ResponseEntity.ok(userConverterService.toDTO(user));
    }

    /**
     * Changes the gender of the user to some other value.
     *
     * @param changeGenderModel the change gender model, it contains the user and the new gender.
     * @return the updated userDTO.
     */
    @PutMapping("/change_gender")
    public ResponseEntity<UserDTO> changeGender(@Valid @NotNull ChangeGenderModel changeGenderModel) {
        var realUser = userConverterService.toDatabaseEntity(changeGenderModel.getUser());
        realUser.setGender(changeGenderModel.getGender());
        var savedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(savedUser));
    }

    /**
     * The add availability endpoint. It adds an availability interval to
     * the user collection of intervals.
     *
     * @param addAvailabilityModel the model, it contains the user and the new interval.
     * @return the updated userDTO.
     */
    @PostMapping("/add_availability")
    public ResponseEntity<UserDTO> addAvailability(@Valid @NotNull AddAvailabilityModel addAvailabilityModel) {
        var realUser = userConverterService.toDatabaseEntity(addAvailabilityModel.getUser());
        realUser.addAvailableTime(addAvailabilityModel.getDateInterval());
        var updatedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(updatedUser));
    }

    /**
     * The remove availability model. It removes an availability from the user collection.
     *
     * @param removeAvailabilityModel the model that contains the user and the availability.
     * @return the updated DTO.
     */
    @DeleteMapping("/remove_availability")
    public ResponseEntity<UserDTO> removeAvailability(@Valid @NotNull RemoveAvailabilityModel removeAvailabilityModel) {
        var realUser = userConverterService.toDatabaseEntity(removeAvailabilityModel.getUser());
        realUser.removeAvailableTime(removeAvailabilityModel.getDateInterval());
        var updatedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(updatedUser));
    }

    /**
     * Adds a new certificate to the user collection.
     *
     * @param addCertificateUserModel the model that contains the user and certificate.
     * @return the updated dto.
     * @throws CertificateNotFoundException if the certificate was not valid or not found.
     */
    @PostMapping("/add_certificate")
    public ResponseEntity<UserDTO> addCertificate(@Valid @NotNull AddCertificateUserModel addCertificateUserModel)
            throws CertificateNotFoundException {
        var realUser = userConverterService.toDatabaseEntity(addCertificateUserModel.getUser());
        realUser.addCertificate(new Certificate(addCertificateUserModel.getCertificate(), null));
        var updatedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(updatedUser));
    }
}