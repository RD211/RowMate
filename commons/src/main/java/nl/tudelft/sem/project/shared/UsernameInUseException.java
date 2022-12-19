package nl.tudelft.sem.project.shared;

/**
 * Exception thrown when a username is already used.
 */
public class UsernameInUseException extends RuntimeException {

    private static final long serialVersionUID = -23434892348923L;

    /**
     * UsernameInUseException constructor.
     *
     * @param message The message to be displayed.
     */
    public UsernameInUseException(String message) {
        super(message);
    }

    /**
     * UsernameInUseException constructor.
     *
     * @param message The message to be displayed.
     * @param cause The cause of the exception.
     */
    public UsernameInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
