package nl.tudelft.sem.project.entities.notifications.mailTemplates;

public class MailSignUp implements MailTemplate {

    String message = "You have successfully managed to sign" +
            "up with a new account! If this was not you," +
            "please reply back to this email.";

    String subject = "Successfully Signed up to Rowing Delft!";

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getSubject() {
        return subject;
    }
}
