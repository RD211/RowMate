package nl.tudelft.sem.project.entities.notifications.mailTemplates;

public class MailUserJoined implements MailTemplate {

    String message = "A user has joined one of the activities" +
            "you are hosting!";

    String subject = "User joined your activity";

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getSubject() {
        return subject;
    }
}
