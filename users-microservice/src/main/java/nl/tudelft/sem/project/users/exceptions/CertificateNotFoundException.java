package nl.tudelft.sem.project.users.exceptions;

import nl.tudelft.sem.project.users.domain.certificate.CertificateName;

import java.util.UUID;

import static java.lang.String.format;

public class CertificateNotFoundException extends RuntimeException {

    static final long serialVersionUID = -3387516993124948L;

    public CertificateNotFoundException(CertificateName name) {
        super(
                format("Certificate with name=\"%s\" could not be found", name.getValue())
        );
    }

    public CertificateNotFoundException(UUID id) {
        super(
                format("Certificate with id=\"%s\" could not be found", id)
        );
    }
}
