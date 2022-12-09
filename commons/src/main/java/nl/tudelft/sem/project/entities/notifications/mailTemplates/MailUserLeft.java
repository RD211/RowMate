package nl.tudelft.sem.project.entities.notifications.mailTemplates;

public class MailUserLeft implements MailTemplate {

    String message = "One user has left your activity.";

    String subject = "User left your activity";

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getSubject() {
        return subject;
    }
}
