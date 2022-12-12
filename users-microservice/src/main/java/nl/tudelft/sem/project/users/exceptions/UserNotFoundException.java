package nl.tudelft.sem.project.users.exceptions;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -1353465654432L;

    /**
     * UserNotFoundException constructor.
     *
     * @param message The message to be displayed.
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * UserNotFoundException constructor.
     *
     * @param message The message to be displayed.
     * @param cause The cause of the exception.
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
