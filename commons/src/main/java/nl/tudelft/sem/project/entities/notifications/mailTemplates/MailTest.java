package nl.tudelft.sem.project.entities.notifications.mailTemplates;

public class MailTest implements MailTemplate {

    String message = "Test email.";

    String subject = "Test email.";
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getSubject() {
        return subject;
    }
}
