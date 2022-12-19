package nl.tudelft.sem.project.users.controllers;

import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import nl.tudelft.sem.project.users.domain.certificate.CertificateConverterService;
import nl.tudelft.sem.project.users.domain.certificate.CertificateName;
import nl.tudelft.sem.project.users.domain.certificate.CertificateService;
import nl.tudelft.sem.project.users.exceptions.CertificateNameInUseException;
import nl.tudelft.sem.project.users.exceptions.CertificateNotFoundException;
import nl.tudelft.sem.project.users.models.ChangeCertificateNameModel;
import nl.tudelft.sem.project.users.models.ChangeCertificateSupersededModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {
    @Autowired
    transient CertificateService certificateService;

    @Autowired
    transient CertificateConverterService certificateConverterService;
    @Autowired
    private CertificateRepository certificateRepository;


    @PostMapping("/add_certificate")
    public ResponseEntity<CertificateDTO> addCertificate(
            @NotNull @RequestBody CertificateDTO certificateDTO)
            throws CertificateNameInUseException, CertificateNotFoundException, ConstraintViolationException {
        var certificate = certificateConverterService.toEntity(certificateDTO);
        var savedCertificate = certificateService.addCertificate(certificate);
        return ResponseEntity.ok(certificateConverterService.toDTO(savedCertificate));
    }

    @GetMapping("/get_certificate_by_id")
    public ResponseEntity<CertificateDTO> getCertificateById(@NotNull @RequestParam UUID certficateId)
            throws CertificateNotFoundException {
        var certificate = certificateService.getCertificateById(certficateId);
        return ResponseEntity.ok(certificateConverterService.toDTO(certificate));
    }


    @GetMapping("/get_certificate_by_name")
    public ResponseEntity<CertificateDTO> getCertificateByName(@NotNull @RequestParam CertificateName certificateName)
            throws CertificateNotFoundException {
        var certificate = certificateService.getCertificateByName(certificateName);
        return ResponseEntity.ok(certificateConverterService.toDTO(certificate));
    }

    @PutMapping("/change_certificate_name")
    public ResponseEntity<CertificateDTO> changeCertificateName(
            @Valid @NotNull @RequestBody ChangeCertificateNameModel changeCertificateNameModel)
            throws CertificateNotFoundException, ConstraintViolationException, CertificateNameInUseException {
        var realCertificate = certificateConverterService.toDatabaseEntity(changeCertificateNameModel.getCertificateDTO());
        realCertificate.setName(new CertificateName(changeCertificateNameModel.getNewName()));
        var updatedCertificate = certificateService.updateCertificate(realCertificate);
        return ResponseEntity.ok(certificateConverterService.toDTO(updatedCertificate));
    }

    @PutMapping("/change_certificate_superseded")
    public ResponseEntity<CertificateDTO> changeCertificateSuperseded(
            @Valid @NotNull @RequestBody ChangeCertificateSupersededModel changeCertificateSupersededModel)
            throws CertificateNotFoundException {
        var realCertificate = certificateConverterService.toDatabaseEntity(
                changeCertificateSupersededModel.getCertificateDTO());
        var newSupersededId = changeCertificateSupersededModel.getNewSupersededId();
        var newSupersededCertificate =
                (newSupersededId == null ? null : certificateService.getCertificateById(newSupersededId));
        realCertificate.setSuperseded(newSupersededCertificate);
        var updatedCertificate = certificateService.updateCertificate(realCertificate);
        return ResponseEntity.ok(certificateConverterService.toDTO(updatedCertificate));
    }

}