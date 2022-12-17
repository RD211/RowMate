package nl.tudelft.sem.project.activities.exceptions;

/**
 * Runtime exception used when a boat was not found in a given context.
 */
public class BoatNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 80850L;

    /**
     * Default constructor.
     */
    public BoatNotFoundException() {
        super("There was no such boat.");
    }

    /**
     * Constructor with a message.
     *
     * @param message Message to display when the exception is thrown.
     */
    public BoatNotFoundException(String message) {
        super(message);
    }
}
