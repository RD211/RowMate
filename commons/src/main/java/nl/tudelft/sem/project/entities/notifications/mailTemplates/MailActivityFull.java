package nl.tudelft.sem.project.entities.notifications.mailTemplates;

public class MailActivityFull implements MailTemplate{

    String message = "The activity you are hosting is now full!";

    String subject = "Hosted activity full!";

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getSubject() {
        return subject;
    }
}
