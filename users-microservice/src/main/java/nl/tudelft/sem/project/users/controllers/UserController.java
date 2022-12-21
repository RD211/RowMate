package nl.tudelft.sem.project.users.controllers;

import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UserEmail;
import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import nl.tudelft.sem.project.users.database.repositories.UserRepository;
import nl.tudelft.sem.project.users.domain.certificate.Certificate;
import nl.tudelft.sem.project.users.domain.certificate.CertificateConverterService;
import nl.tudelft.sem.project.users.domain.certificate.CertificateService;
import nl.tudelft.sem.project.users.exceptions.CertificateNotFoundException;
import nl.tudelft.sem.project.users.domain.users.*;
import nl.tudelft.sem.project.users.models.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    transient CertificateService certificateService;
    @Autowired
    transient UserConverterService userConverterService;
    @Autowired
    transient CertificateRepository certificateRepository;

    @Autowired
    transient CertificateConverterService certificateConverterService;


    /**
     * The add user endpoint. It adds a user to the database given the UserDTO.
     *
     * @param userDTO the user dto, the data that will be added to the database.
     * @return the updated dto including the ID.
     */
    @PostMapping("/add_user")
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO) {
        var user = userConverterService.toEntity(userDTO);
        var savedUser = userService.addUser(user);
        return ResponseEntity.ok(userConverterService.toDTO(savedUser));
    }

    /**
     * Gets a userDTO by the username.
     *
     * @param username the requested username.
     * @return the userDTO.
     */
    @GetMapping("/get_user_by_username")
    public ResponseEntity<UserDTO> getUserByUsername(@Valid @NotNull @RequestParam("username") Username username) {
        var user = userService.getUserByUsername(username);
        return ResponseEntity.ok(userConverterService.toDTO(user));
    }

    /**
     * Changes the gender of the user to some other value.
     *
     * @param changeGenderUserModel the change gender model, it contains the user and the new gender.
     * @return the updated userDTO.
     */
    @PutMapping("/change_gender")
    public ResponseEntity<UserDTO> changeGender(
            @Valid @NotNull @RequestBody ChangeGenderUserModel changeGenderUserModel) {
        var realUser = userConverterService.toDatabaseEntity(changeGenderUserModel.getUser());
        realUser.setGender(changeGenderUserModel.getGender());
        var savedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(savedUser));
    }

    /**
     * Changes the organization of the user to some other value.
     *
     * @param changeOrganizationUserModel the change organization model, it contains the user and the new organization.
     * @return the updated userDTO.
     */
    @PutMapping("/change_organization")
    public ResponseEntity<UserDTO> changeOrganization(
            @Valid @NotNull @RequestBody ChangeOrganizationUserModel changeOrganizationUserModel) {
        var realUser = userConverterService.toDatabaseEntity(changeOrganizationUserModel.getUser());
        realUser.setOrganization(changeOrganizationUserModel.getOrganization());
        var savedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(savedUser));
    }

    /**
     * Changes the skill level of the user.
     * (Changes the boolean flag for isAmateur to being either true or false)
     *
     * @param changeAmateurUserModel the model that contains the user and the new value.
     * @return the updated user dto.
     */
    @PutMapping("/change_amateur")
    public ResponseEntity<UserDTO> changeAmateur(
            @Valid @NotNull @RequestBody ChangeAmateurUserModel changeAmateurUserModel) {
        var realUser = userConverterService.toDatabaseEntity(changeAmateurUserModel.getUser());
        realUser.setAmateur(changeAmateurUserModel.isAmateur());
        var savedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(savedUser));
    }

    /**
     * The add availability endpoint. It adds an availability interval to
     * the user collection of intervals.
     *
     * @param addAvailabilityUserModel the model, it contains the user and the new interval.
     * @return the updated userDTO.
     */
    @PostMapping("/add_availability")
    public ResponseEntity<UserDTO> addAvailability(
            @Valid @NotNull @RequestBody AddAvailabilityUserModel addAvailabilityUserModel) {
        var realUser = userConverterService.toDatabaseEntity(addAvailabilityUserModel.getUser());
        realUser.addAvailableTime(addAvailabilityUserModel.getDateInterval());
        var updatedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(updatedUser));
    }

    /**
     * The remove availability model. It removes an availability from the user collection.
     *
     * @param removeAvailabilityUserModel the model that contains the user and the availability.
     * @return the updated DTO.
     */
    @DeleteMapping("/remove_availability")
    public ResponseEntity<UserDTO> removeAvailability(
            @Valid @NotNull @RequestBody RemoveAvailabilityUserModel removeAvailabilityUserModel) {
        var realUser = userConverterService.toDatabaseEntity(removeAvailabilityUserModel.getUser());
        realUser.removeAvailableTime(removeAvailabilityUserModel.getDateInterval());
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
    public ResponseEntity<UserDTO> addCertificate(
            @Valid @NotNull @RequestBody AddCertificateUserModel addCertificateUserModel)
            throws CertificateNotFoundException {
        var certificate =
                certificateRepository.findById(addCertificateUserModel.getCertificate().getId());
        if (certificate.isEmpty()) {
            throw new CertificateNotFoundException(addCertificateUserModel.getCertificate().getId());
        }

        var realUser = userConverterService.toDatabaseEntity(addCertificateUserModel.getUser());
        realUser.addCertificate(
                certificateConverterService.toDatabaseEntity(addCertificateUserModel.getCertificate()));
        var updatedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(updatedUser));
    }

    /**
     * Removes a certificate from the users' collection.
     *
     * @param removeCertificateUserModel the model that contains the user and the certificate to remove.
     * @return the updated user.
     * @throws CertificateNotFoundException in case the certificate does not exist in the database.
     */
    @DeleteMapping("/remove_certificate")
    public ResponseEntity<UserDTO> removeCertificate(
            @Valid @NotNull @RequestBody RemoveCertificateUserModel removeCertificateUserModel)
            throws CertificateNotFoundException {
        var certificate =
                certificateRepository.findById(removeCertificateUserModel.getCertificate().getId());
        if (certificate.isEmpty()) {
            throw new CertificateNotFoundException(removeCertificateUserModel.getCertificate().getId());
        }

        var realUser = userConverterService.toDatabaseEntity(removeCertificateUserModel.getUser());
        realUser.removeCertificate(
                certificateConverterService.toDatabaseEntity(removeCertificateUserModel.getCertificate()));
        var updatedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(updatedUser));
    }

    /**
     * Adds a new role to the user collection.
     *
     * @param addRoleUserModel the model that contains the user and the role.
     * @return the updated dto.
     */
    @PostMapping("/add_role")
    public ResponseEntity<UserDTO> addRole(@Valid @NotNull @RequestBody AddRoleUserModel addRoleUserModel) {
        var realUser = userConverterService.toDatabaseEntity(addRoleUserModel.getUser());
        realUser.addBoatRole(addRoleUserModel.getRole());
        var updatedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(updatedUser));
    }

    /**
     * Removes a role from the users collection.
     *
     * @param removeRoleUserModel the model that contains the user and the role to remove.
     * @return the updated user.
     */
    @DeleteMapping("/remove_role")
    public ResponseEntity<UserDTO> removeRole(
            @Valid @NotNull @RequestBody RemoveRoleUserModel removeRoleUserModel) {
        var realUser = userConverterService.toDatabaseEntity(removeRoleUserModel.getUser());
        realUser.removeBoatRole(removeRoleUserModel.getRole());
        var updatedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(updatedUser));
    }

    /**
     * Delete a user by the username.
     * This is an admin endpoint.
     *
     * @param username the username of the user to delete.
     * @return the deleted user before the action.
     */
    @DeleteMapping("/delete_user_by_username")
    public ResponseEntity<Void> deleteUserByUsername(
            @Valid @NotNull @RequestBody Username username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok().build();
    }


    /**
     * Delete a user by the email.
     * This is an admin endpoint.
     *
     * @param email the email of the user to delete.
     * @return the deleted user before the action.
     */
    @DeleteMapping("/delete_user_by_email")
    public ResponseEntity<Void> deleteUserByEmail(
            @Valid @NotNull @RequestBody UserEmail email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok().build();
    }

    /**
     * Checks whether a user is in possession of a certificate directly or through supersedence.
     *
     * @param username The user to check for.
     * @param certificateId The certificate to look for.
     * @return Whether the user has the certificate.
     */
    @SuppressWarnings("PMD")
    @GetMapping("/has_certificate")
    public ResponseEntity<Boolean> hasCertificate(@Valid @NotNull @RequestParam("username") Username username,
                                                  @Valid @NotNull @RequestParam("certificateId") UUID certificateId) {
        var realUser = userService.getUserByUsername(username);
        var realCertificate = certificateService.getCertificateById(certificateId);
        for (Certificate cert : realUser.getCertificates()) {
            if (cert.hasInChain(realCertificate)) {
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.ok(false);
    }
}