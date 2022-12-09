package nl.tudelft.sem.project.entities.notifications.mailTemplates;

public class MailResetPassword implements MailTemplate {

    String message = "Your account password has just been" +
            "reset. If this was not you, please contact" +
            "us immediately.";

    String subject = "Password reset";

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getSubject() {
        return subject;
    }
}
