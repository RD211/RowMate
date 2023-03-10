package nl.tudelft.sem.project.notifications.mailTemplates.config;

import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.mailTemplates.MailTemplateImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailTemplateConfig {

    @Bean
    public MailTemplateImpl getMailActivityFull() {
        return MailTemplateImpl
                .builder()
                .subject("Hosted activity full!")
                .message("The activity you are hosting is now full!")
                .eventType(EventType.ACTIVITY_FULL)
                .build();
    }

    @Bean
    public MailTemplateImpl getMailActivityStartSoon() {
        return MailTemplateImpl
                .builder()
                .subject("Activity starting soon!")
                .message("One of the activities you have signed up for " +
                        "will start soon!")
                .eventType(EventType.ACTIVITY_START_SOON)
                .build();
    }

    @Bean
    public MailTemplateImpl getMailJoinActivity() {
        return MailTemplateImpl
                .builder()
                .subject("Activity joined")
                .message("Your request to join an activity has been accepted!")
                .eventType(EventType.JOINED_ACTIVITY)
                .build();
    }

    @Bean
    public MailTemplateImpl getMailRejectJoin() {
        return MailTemplateImpl
                .builder()
                .subject("Request declined :(")
                .message("Your request to join an activity has been declined!")
                .eventType(EventType.REJECT_REGISTRATION)
                .build();
    }


    @Bean
    public MailTemplateImpl getMailResetPassword() {
        return MailTemplateImpl
                .builder()
                .subject("Reset your account password")
                .message("A request to change the password for this " +
                        "account has been done. If this was not you, " +
                        "please ignore this email.")
                .eventType(EventType.RESET_PASSWORD)
                .build();
    }

    @Bean
    public MailTemplateImpl getMailSignUp() {
        return MailTemplateImpl
                .builder()
                .subject("Account successfully created!")
                .message("You have successfully managed to sign " +
                        "up with a new account! If this was not you," +
                        " please reply back to this email.")
                .eventType(EventType.SIGN_UP)
                .build();
    }

    @Bean
    public MailTemplateImpl getMailTest() {
        return MailTemplateImpl
                .builder()
                .subject("Test email")
                .message("Test email.")
                .eventType(EventType.TEST)
                .build();
    }

    @Bean
    public  MailTemplateImpl getMailTestActivity() {
        return MailTemplateImpl
                .builder()
                .subject("Test email")
                .message("Test email.")
                .eventType(EventType.TEST_ACTIVITY)
                .build();
    }

    @Bean
    public MailTemplateImpl getMailUserJoined() {
        return MailTemplateImpl
                .builder()
                .subject("User joined your activity")
                .message("A user has requested to join one of the activities " +
                        "you are hosting! Go see their request!")
                .eventType(EventType.USER_JOINED)
                .build();
    }

    @Bean
    public MailTemplateImpl getMailUserLeft() {
        return MailTemplateImpl
                .builder()
                .subject("User left your activity")
                .message("One user has left your activity.")
                .eventType(EventType.USER_LEFT)
                .build();
    }

    @Bean
    public MailTemplateImpl getMailActivityCreated() {
        return MailTemplateImpl
                .builder()
                .subject("Activity created")
                .message("You have successfully created a new activity!")
                .eventType(EventType.CREATED_ACTIVITY)
                .build();
    }

    @Bean
    public MailTemplateImpl getMailConfirmPasswordReset() {
        return MailTemplateImpl
                .builder()
                .subject("Password reset")
                .message("Your account password has just been reset. " +
                        "If this was not you, please contact us immediately.")
                .eventType(EventType.RESET_PASSWORD_CONFIRM)
                .build();
    }
}
