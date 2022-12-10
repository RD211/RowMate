package nl.tudelft.sem.project.users.exceptions;

/**
 * Exception thrown when a username is already used.
 */
public class UsernameInUseException extends RuntimeException {

    private static final long serialVersionUID = 11L;

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
