package nl.tudelft.sem.project.users.controllers;

import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import nl.tudelft.sem.project.users.domain.certificate.Certificate;
import nl.tudelft.sem.project.users.domain.certificate.CertificateConverterService;
import nl.tudelft.sem.project.users.domain.certificate.CertificateName;
import nl.tudelft.sem.project.users.domain.certificate.CertificateService;
import nl.tudelft.sem.project.users.exceptions.CertificateNameInUseException;
import nl.tudelft.sem.project.users.exceptions.CertificateNotFoundException;
import nl.tudelft.sem.project.users.models.ChangeCertificateNameModel;
import nl.tudelft.sem.project.users.models.ChangeCertificateSupersededModel;
import nl.tudelft.sem.project.utils.Fictional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {
    @Autowired
    transient CertificateService certificateService;

    @Autowired
    transient CertificateConverterService certificateConverterService;


    /**
     * Endpoint to add new certificates to the system.
     *
     * @param certificateDTO The new certificate to be added to the system.
     * @return The certificate that was added along with its new id.
     * @throws CertificateNameInUseException Is thrown when a certificate with a given name already exists.
     * @throws CertificateNotFoundException Is thrown when the superseding certificate did not exist.
     * @throws ConstraintViolationException Is thrown when an invalid certificate name is passed.
     */
    @PostMapping("/add_certificate")
    public ResponseEntity<CertificateDTO> addCertificate(
            @Valid @Validated(Fictional.class) @RequestBody CertificateDTO certificateDTO)
            throws CertificateNameInUseException, CertificateNotFoundException, ConstraintViolationException {
        var certificate = certificateConverterService.toEntity(certificateDTO);
        var savedCertificate = certificateService.addCertificate(certificate);
        return ResponseEntity.ok(certificateConverterService.toDTO(savedCertificate));
    }

    /**
     * Endpoint to fetch a certificate by its id.
     *
     * @param certficateId The id of the certificate to be fetched.
     * @return The found certificate.
     * @throws CertificateNotFoundException Is thrown when the certificate could not be found.
     */
    @GetMapping("/get_certificate_by_id")
    public ResponseEntity<CertificateDTO> getCertificateById(@NotNull @RequestParam UUID certficateId)
            throws CertificateNotFoundException {
        var certificate = certificateService.getCertificateById(certficateId);
        return ResponseEntity.ok(certificateConverterService.toDTO(certificate));
    }

    /**
     * Endpoint to fetch a certificate by its name.
     *
     * @param certificateName The name of the certificate to be fetched.
     * @return The found certificate.
     * @throws CertificateNotFoundException Is thrown when the certificate could not be found.
     */
    @GetMapping("/get_certificate_by_name")
    public ResponseEntity<CertificateDTO> getCertificateByName(@NotNull @RequestParam CertificateName certificateName)
            throws CertificateNotFoundException {
        var certificate = certificateService.getCertificateByName(certificateName);
        return ResponseEntity.ok(certificateConverterService.toDTO(certificate));
    }

    /**
     * Endpoint to update the certificate name.
     *
     * @param changeCertificateNameModel The model that specifies the certificate and its new name.
     * @return The updated certificate.
     * @throws CertificateNotFoundException Is thrown when the certificate to be updated does not exist.
     * @throws ConstraintViolationException Is thrown when the new name is invalid.
     * @throws CertificateNameInUseException Is thrown when the new name was already in use.
     */
    @PutMapping("/change_certificate_name")
    public ResponseEntity<CertificateDTO> changeCertificateName(
            @Valid @NotNull @RequestBody ChangeCertificateNameModel changeCertificateNameModel)
            throws CertificateNotFoundException, ConstraintViolationException, CertificateNameInUseException {
        var realCertificate = certificateConverterService.toDatabaseEntity(changeCertificateNameModel.getCertificateDTO());
        var updatedCertificate = certificateService.updateCertificateName(
                realCertificate, new CertificateName(changeCertificateNameModel.getNewName()));
        return ResponseEntity.ok(certificateConverterService.toDTO(updatedCertificate));
    }

    /**
     * Endpoint to update the certificate's superseded certificate.
     *
     * @param changeCertificateSupersededModel The model that specifies the certificate and its new superseded one.
     * @return The updated certificate.
     * @throws CertificateNotFoundException Is thrown when either the new superseding or the to be updated certificates
     *                                      do not exist.
     */
    @PutMapping("/change_certificate_superseded")
    public ResponseEntity<CertificateDTO> changeCertificateSuperseded(
            @Valid @NotNull @RequestBody ChangeCertificateSupersededModel changeCertificateSupersededModel)
            throws CertificateNotFoundException {
        var realCertificate = certificateConverterService.toDatabaseEntity(
                changeCertificateSupersededModel.getCertificateDTO());
        var updatedCertificate = certificateService.updateCertificateSuperseded(
                realCertificate, changeCertificateSupersededModel.getNewSupersededId());
        return ResponseEntity.ok(certificateConverterService.toDTO(updatedCertificate));
    }

    /**
     * Endpoint to fetch all available certificates.
     *
     * @return The list of all certificates stored in the repository.
     */
    @GetMapping("")
    public ResponseEntity<List<CertificateDTO>> getAllAvailableCertificates() {
        return ResponseEntity.ok(
                certificateService.getAllCertificates()
                        .stream()
                        .map(c -> certificateConverterService.toDTO(c)).collect(Collectors.toList()));

    }

}