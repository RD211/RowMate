package nl.tudelft.sem.project.entities.notifications.mailTemplates;

public class MailActivityStartSoon implements MailTemplate{

    String message = "One of the activities you have signed up for" +
            "will start soon!";

    String subject = "Activity starting soon!";

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getSubject() {
        return subject;
    }
}
