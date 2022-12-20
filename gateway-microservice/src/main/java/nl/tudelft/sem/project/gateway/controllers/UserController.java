package nl.tudelft.sem.project.gateway.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.shared.Organization;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.CertificatesClient;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import nl.tudelft.sem.project.users.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/user")
public class UserController {

    private final transient AuthManager authManager;

    private final transient UsersClient usersClient;

    private final transient CertificatesClient certificatesClient;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public UserController(AuthManager authManager, UsersClient usersClient, CertificatesClient certificatesClient) {
        this.authManager = authManager;
        this.usersClient = usersClient;
        this.certificatesClient = certificatesClient;
    }


    /**
     * Gets details about the currently logged-in user.
     *
     * @return the user dto of the user.
     */
    @GetMapping("/get_details")
    public ResponseEntity<UserDTO> getDetailsOfUser() {
        var username = authManager.getUsername();
        var userDetails = usersClient.getUserByUsername(new Username(username));
        return ResponseEntity.ok(userDetails);
    }

    /**
     * Sets the gender of the currently logged-in user.
     *
     * @param gender the new gender.
     * @return the updated user dto.
     */
    @PostMapping("/change_gender")
    public ResponseEntity<UserDTO> changeGender(@Valid @RequestBody @NotNull Gender gender) {
        var username = authManager.getUsername();
        var response = usersClient.changeGenderOfUser(
                new ChangeGenderUserModel(
                        UserDTO.builder().username(username).build(),
                        gender
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Changes the organization of the currently logged-in user.
     *
     * @param organization the new organization.
     * @return the updated user dto.
     */
    @PostMapping("/change_organization")
    public ResponseEntity<UserDTO> changeOrganization(@Valid @RequestBody @NotNull Organization organization) {
        var username = authManager.getUsername();
        var response = usersClient.changeOrganizationOfUser(
                new ChangeOrganizationUserModel(
                        UserDTO.builder().username(username).build(),
                        organization
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Changes the amateur flag of the currently logged-in user.
     *
     * @param isAmateur the new value of the flag.
     * @return the updated user dto.
     */
    @PostMapping("/change_amateur")
    public ResponseEntity<UserDTO> changeIfUserIsAmateur(@Valid @RequestBody @NotNull boolean isAmateur) {
        var username = authManager.getUsername();
        var response = usersClient.changeAmateurOfUser(
                new ChangeAmateurUserModel(
                        UserDTO.builder().username(username).build(),
                        isAmateur
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Adds an availability to the user set.
     *
     * @param dateInterval the new date interval.
     * @return the updated user dto.
     */
    @PostMapping("/add_availability")
    public ResponseEntity<UserDTO> addAvailability(@Valid @RequestBody @NotNull DateInterval dateInterval) {
        var username = authManager.getUsername();
        var response = usersClient.addAvailabilityToUser(
                new AddAvailabilityUserModel(
                        UserDTO.builder().username(username).build(),
                        dateInterval
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Removes an interval from the user set.
     *
     * @param dateInterval the interval to remove.
     * @return the updated user dto.
     */
    @PostMapping("/remove_availability")
    public ResponseEntity<UserDTO> removeAvailability(@Valid @RequestBody @NotNull DateInterval dateInterval) {
        var username = authManager.getUsername();
        var response = usersClient.removeAvailabilityOfUser(
                new RemoveAvailabilityUserModel(
                        UserDTO.builder().username(username).build(),
                        dateInterval
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Adds a role to the user set.
     *
     * @param boatRole the role to add.
     * @return the updated user dto.
     */
    @PostMapping("/add_role")
    public ResponseEntity<UserDTO> addRole(@Valid @RequestBody @NotNull BoatRole boatRole) {
        var username = authManager.getUsername();
        var response = usersClient.addRoleToUser(
                new AddRoleUserModel(
                        UserDTO.builder().username(username).build(),
                        boatRole
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Removes a role from the user set.
     *
     * @param boatRole the role to remove.
     * @return the updated user dto.
     */
    @PostMapping("/remove_role")
    public ResponseEntity<UserDTO> removeRole(@Valid @RequestBody @NotNull BoatRole boatRole) {
        var username = authManager.getUsername();
        var response = usersClient.removeRoleFromUser(
                new RemoveRoleUserModel(
                        UserDTO.builder().username(username).build(),
                        boatRole
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Adds a new certificate to the user set of certificates.
     *
     * @param certificateID the new certificate to add.
     * @return the updated user dto.
     */
    @PostMapping("/add_certificate")
    public ResponseEntity<UserDTO> addCertificate(@Valid @RequestBody @NotNull UUID certificateID) {
        var username = authManager.getUsername();
        var response = usersClient.addCertificateToUser(
                new AddCertificateUserModel(
                        UserDTO.builder().username(username).build(),
                        certificatesClient.getCertificateById(certificateID)
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Removes a certificates from the user set.
     *
     * @param certificateID the certificate to remove.
     * @return the updated user dto.
     */
    @PostMapping("/remove_certificate")
    public ResponseEntity<UserDTO> removeCertificate(@Valid @RequestBody @NotNull UUID certificateID) {
        var username = authManager.getUsername();
        var response = usersClient.removeCertificateFromUser(
                new RemoveCertificateUserModel(
                        UserDTO.builder().username(username).build(),
                        certificatesClient.getCertificateById(certificateID)
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Checks whether a user is in possession of a certificate directly or through supersedence.
     *
     * @param certificateId The certificate to look for.
     * @return Whether the user has the certificate.
     */
    @GetMapping("/has_certificate")
    public ResponseEntity<Boolean> hasCertificate(
            @Valid @NotNull @RequestBody UUID certificateId) {
        var username = authManager.getUsername();
        return ResponseEntity.ok(usersClient.hasCertificate(new Username(username), certificateId));
    }
}
