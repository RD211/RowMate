package nl.tudelft.sem.project.users.domain.certificate;

import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import org.springframework.stereotype.Service;

/**
 * A DDD service for managing certificate.
 */
@Service
public class CertificateService {
    private final transient CertificateRepository certificateRepository;

    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public Certificate addCertificate() {

        return null;
    }

}
