package nl.tudelft.sem.project.users.exceptions;

import nl.tudelft.sem.project.users.CertificateName;

import static java.lang.String.format;

/**
 * Exception thrown when a certificate with this name is already in the database.
 */
public class CertificateNameInUseException extends RuntimeException {

    private static final long serialVersionUID = -2343642383269432L;

    /**
     * Exception constructor that takes the name of the certificate that already exists.
     *
     * @param name The name to be included in the exception message.
     */
    public CertificateNameInUseException(CertificateName name) {
        super(
                format("Certificate with name=\"%s\" already exists", name.getValue())
        );
    }

}
