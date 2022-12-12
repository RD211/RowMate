package nl.tudelft.sem.project.notifications.exceptions;

public class MailNotSentException extends Exception {

    private static final long serialVersionUID = 10L;

    public MailNotSentException(String message) {
        super(message);
    }
}
