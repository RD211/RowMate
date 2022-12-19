package nl.tudelft.sem.project.activities.exceptions;

/**
 * Runtime exception used when a role was not found in a given context.
 */
public class RoleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 80851L;

    /**
     * Default constructor.
     */
    public RoleNotFoundException() {
        super("There was no such role.");
    }

    /**
     * Constructor with a message.
     *
     * @param message Message to display when the exception is thrown.
     */
    public RoleNotFoundException(String message) {
        super(message);
    }
}
