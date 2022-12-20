package nl.tudelft.sem.project.users.domain.certificate;

import lombok.NonNull;
import nl.tudelft.sem.project.users.CertificateName;
import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import nl.tudelft.sem.project.users.exceptions.CertificateNameInUseException;
import nl.tudelft.sem.project.users.exceptions.CertificateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;


/**
 * A DDD service for managing certificate.
 */
@Service
@Validated
public class CertificateService {
    @Autowired
    transient CertificateRepository certificateRepository;

    /**
     * Adds a Certificate to the repository.
     * Throws when the given certificate's name is already in the repository.
     * Assumes that the superseded if present is a valid entity from the database (previously fetched from the repo).
     *
     * @param certificate A non-null certificate to be saved in the repository.
     * @return The saved version of the certificate with the updated id.
     * @throws CertificateNameInUseException Is thrown when one tries to save a certificate with a name
     *                                       that is already in the repository.
     */
    public Certificate addCertificate(@NonNull Certificate certificate)
            throws CertificateNameInUseException {
        if (existsByName(certificate.getName())) {
            throw new CertificateNameInUseException(certificate.getName());
        }
        var superseded = certificate.getSuperseded();
        if (superseded.isPresent() && !existsById(superseded.get().getId())) {
            throw new CertificateNotFoundException(superseded.get().getId());
        }

        return certificateRepository.save(certificate);
    }

    /**
     * Gets a certificate with a given id from the repository.
     *
     * @param id The identifier to look for with.
     * @return The certificate with the corresponding identifier.
     * @throws CertificateNotFoundException Is thrown when the certificate could not be found in the repository.
     */
    public Certificate getCertificateById(@NonNull UUID id) throws CertificateNotFoundException {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new CertificateNotFoundException(id));
    }

    /**
     * Gets a certificate with a given name from the repository.
     *
     * @param name The name to look for with.
     * @return The certificate with the corresponding name.
     * @throws CertificateNotFoundException Is thrown when the certificate could not be found in the repository.
     */
    public Certificate getCertificateByName(@NonNull CertificateName name) throws CertificateNotFoundException {
        return certificateRepository.findByName(name)
                .orElseThrow(() -> new CertificateNotFoundException(name));
    }

    /**
     * Checks whether a certificate with this id already exists.
     *
     * @param id The id to check.
     * @return True iff the certificate already exists.
     */
    public boolean existsById(@NonNull UUID id) {
        return certificateRepository.existsById(id);
    }

    /**
     * Checks whether a certificate with this name already exists.
     *
     * @param name The certificate name to check.
     * @return True iff the certificate already exists.
     */
    public boolean existsByName(@NonNull CertificateName name) {
        return certificateRepository.existsByName(name);
    }

    /**
     * Used to update a certificate's name.
     *
     * @param certificate The certificate to be updated.
     * @param newName The new name to be changed.
     * @return The updated certificate.
     * @throws CertificateNotFoundException Is thrown when the certificate could not be found in the repository
     *                                      by its id.
     * @throws CertificateNameInUseException Is thrown when the name already exists.
     */
    public Certificate updateCertificateName(@NonNull Certificate certificate, @NonNull CertificateName newName)
            throws CertificateNotFoundException, CertificateNameInUseException {

        if (existsByName(newName)) {
            throw new CertificateNameInUseException(newName);
        }

        Certificate realCertificate = getCertificateById(certificate.getId());
        realCertificate.setName(newName);

        return certificateRepository.save(realCertificate);
    }

    /**
     * Used to update a certificate's superseded certificate.
     *
     * @param certificate The new certificate's field values.
     * @param newSuperseded The new superseded certificate. Shall be Empty when there should be no superseding.
     * @return The updated certificate.
     * @throws CertificateNotFoundException Is throws when the certificate could not be found in the repository
     *                                      by its id.
     */
    public Certificate updateCertificateSuperseded(@NonNull Certificate certificate, @NonNull Optional<UUID> newSuperseded)
            throws CertificateNotFoundException {

        if (newSuperseded.isPresent() && !existsById(newSuperseded.get())) {
            throw new CertificateNotFoundException(newSuperseded.get());
        }

        Certificate realCertificate = getCertificateById(certificate.getId());
        realCertificate.setSuperseded(newSuperseded.map(id -> getCertificateById(id)).orElse(null));

        return certificateRepository.save(realCertificate);
    }

    /**
     * Gets all certificates available in the repository.
     *
     * @return A collection of all certificates.
     */
    public Collection<Certificate> getAllCertificates() {
        List<Certificate> res = new ArrayList<>();
        certificateRepository.findAll().forEach(res::add);
        return res;
    }

}
