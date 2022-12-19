package nl.tudelft.sem.project.activities;

/**
 * Exception thrown when a Activity is not found.
 */
public class ActivityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -133465654432L;

    /**
     * ActivityNotFoundException constructor.
     *
     * @param message The message to be displayed.
     */
    public ActivityNotFoundException(String message) {
        super(message);
    }

    /**
     * ActivityNotFoundException constructor.
     *
     * @param message The message to be displayed.
     * @param cause The cause of the exception.
     */
    public ActivityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
