package nl.tudelft.sem.project.users.controllers;

import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import nl.tudelft.sem.project.users.database.repositories.UserRepository;
import nl.tudelft.sem.project.users.domain.certificate.CertificateConverterService;
import nl.tudelft.sem.project.users.domain.certificate.CertificateService;
import nl.tudelft.sem.project.users.domain.users.UserConverterService;
import nl.tudelft.sem.project.users.domain.users.UserService;
import nl.tudelft.sem.project.users.exceptions.CertificateNotFoundException;
import nl.tudelft.sem.project.users.models.AddCertificateUserModel;
import nl.tudelft.sem.project.users.models.RemoveCertificateUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserCertificateController {
    @Autowired
    transient UserService userService;
    @Autowired
    transient CertificateService certificateService;
    @Autowired
    transient UserConverterService userConverterService;
    @Autowired
    transient UserRepository userRepository;
    @Autowired
    transient CertificateConverterService certificateConverterService;

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

        // This checks that the certificate exists
        certificateService.getCertificateById(addCertificateUserModel.getCertificate().getId());

        var realUser = userConverterService.toDatabaseEntity(addCertificateUserModel.getUser());
        realUser.addCertificate(
                certificateConverterService.toDatabaseEntity(addCertificateUserModel.getCertificate()));
        var savedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(savedUser));
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

        // This checks that the certificate exists
        certificateService.getCertificateById(removeCertificateUserModel.getCertificate().getId());

        var realUser = userConverterService.toDatabaseEntity(removeCertificateUserModel.getUser());
        realUser.removeCertificate(
                certificateConverterService.toDatabaseEntity(removeCertificateUserModel.getCertificate()));
        var savedUser = userRepository.save(realUser);
        return ResponseEntity.ok(userConverterService.toDTO(savedUser));
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
                                                  @Valid @RequestParam("certificateId") UUID certificateId) {
        var realUser = userService.getUserByUsername(username);
        var realCertificate = certificateService.getCertificateById(certificateId);

        return ResponseEntity.ok(realUser.getCertificates().stream().anyMatch(
                certificate -> certificate.hasInChain(realCertificate)
        ));
    }
}
