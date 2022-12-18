package nl.tudelft.sem.project.users.exceptions;

import nl.tudelft.sem.project.users.domain.certificate.CertificateName;

import java.util.UUID;

import static java.lang.String.format;

/**
 * Exception thrown when a certificate with this name is already in the database.
 */
public class CertificateNotFoundException extends RuntimeException {

    static final long serialVersionUID = -3387516993124948L;

    /**
     * Exception constructor that takes the name of the certificate that could not be found.
     *
     * @param name The name to be included in the exception message.
     */
    public CertificateNotFoundException(CertificateName name) {
        super(
                format("Certificate with name=\"%s\" could not be found", name.getValue())
        );
    }

    /**
     * Exception constructor that takes the id of the certificate that could not be found.
     *
     * @param id The UUID to be included in the exception message.
     */
    public CertificateNotFoundException(UUID id) {
        super(
                format("Certificate with id=\"%s\" could not be found", id)
        );
    }
}
