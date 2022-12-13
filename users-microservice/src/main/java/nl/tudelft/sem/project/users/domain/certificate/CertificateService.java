package nl.tudelft.sem.project.users.domain.certificate;

import lombok.NonNull;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import nl.tudelft.sem.project.users.exceptions.CertificateNameInUseException;
import nl.tudelft.sem.project.users.exceptions.CertificateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

/**
 * A DDD service for managing certificate.
 */
@Service
@Validated
public class CertificateService {
    @Autowired
    transient CertificateRepository certificateRepository;

    public Certificate addCertificate(@NonNull Certificate certificate) throws CertificateNameInUseException {
        if (certificateRepository.existsByCertificateName(certificate.getName())) {
            throw new CertificateNameInUseException(certificate.getName());
        }

        return certificateRepository.save(certificate);
    }

    public Certificate getCertificateById(@NonNull UUID id) throws CertificateNotFoundException {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new CertificateNotFoundException(id));
    }

    public void updateCertificate(@NonNull Certificate certificate)
            throws CertificateNotFoundException {

        Certificate saved = certificateRepository.findById(certificate.getId()).orElseThrow(() -> new CertificateNotFoundException(certificate.getId()));

        saved.setName(certificate.getName());
        saved.setSuperseded(certificate.getSuperseded().orElse(null));

        certificateRepository.save(saved);
    }

}
