package nl.tudelft.sem.project.users.domain.certificate;

import java.util.UUID;

public class SupersededCertificateDoesNotExistException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    public SupersededCertificateDoesNotExistException(UUID certificateUUID) {
        super(certificateUUID.toString());
    }
}
