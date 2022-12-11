package nl.tudelft.sem.project.users.exceptions;

/**
 * Exception thrown when an email is already used by another user.
 */
public class EmailInUseException extends RuntimeException {

    private static final long serialVersionUID = -13123877854L;

    /**
     * EmailInUseException constructor.
     *
     * @param message The message to be displayed.
     */
    public EmailInUseException(String message) {
        super(message);
    }

    /**
     * EmailInUseException constructor.
     *
     * @param message The message to be displayed.
     * @param cause The cause of the exception.
     */
    public EmailInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
