package nl.tudelft.sem.project.entities.notifications.mailTemplates;

public class MailJoinActivity implements MailTemplate {

    String message = "You have successfully joined the activity!";

    String subject = "Activity joined";

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getSubject() {
        return subject;
    }
}
