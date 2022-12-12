package nl.tudelft.sem.project.users.domain.certificate;

import java.util.UUID;

public class CertificateNotFoundException extends RuntimeException {

    static final long serialVersionUID = -3387516993124948L;

    public CertificateNotFoundException(UUID certificateUUID) {
        super(certificateUUID.toString());
    }
}
