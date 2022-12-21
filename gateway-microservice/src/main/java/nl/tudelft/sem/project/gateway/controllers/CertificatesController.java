package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.CertificateName;
import nl.tudelft.sem.project.users.CertificatesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/certificates")
public class CertificatesController {

    @Autowired
    private transient CertificatesClient certificatesClient;

    /**
     * Endpoint to fetch a certificate by its id.
     *
     * @param certificateId The id of the certificate to be fetched.
     * @return The found certificate.
     */
    @GetMapping("/get_certificate_by_id")
    public ResponseEntity<CertificateDTO> getCertificateById(@NotNull @RequestParam UUID certificateId) {
        var certificate = certificatesClient.getCertificateById(certificateId);
        return ResponseEntity.ok(certificate);
    }

    /**
     * Endpoint to fetch a certificate by its name.
     *
     * @param certificateName The name of the certificate to be fetched.
     * @return The found certificate.
     */
    @GetMapping("/get_certificate_by_name")
    public ResponseEntity<CertificateDTO> getCertificateByName(
            @NotNull @RequestParam("certificateName") CertificateName certificateName) {
        var certificate = certificatesClient.getCertificateByName(certificateName);
        return ResponseEntity.ok(certificate);
    }

    /**
     * Endpoint to fetch all available certificates.
     *
     * @return The list of all certificates stored in the repository.
     */
    @GetMapping("")
    public ResponseEntity<List<CertificateDTO>> getAllAvailableCertificates() {
        var certificates = certificatesClient.getAllAvailableCertificates();
        return ResponseEntity.ok(certificates);
    }

    /**
     * Endpoint to fetch a chain of certificates.
     *
     * @param certificateId Certificate at the start of the chain.
     * @return The ordered list of certificates in the chain.
     */
    @GetMapping("/get_certificate_chain_by_id")
    public ResponseEntity<List<CertificateDTO>> getCertificateChain(@NotNull @RequestParam UUID certificateId) {
        var certificates = certificatesClient.getCertificateChain(certificateId);
        return ResponseEntity.ok(certificates);
    }
}
