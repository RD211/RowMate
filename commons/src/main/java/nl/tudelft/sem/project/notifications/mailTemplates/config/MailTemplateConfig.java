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
                .message("You have successfully joined the activity!")
                .eventType(EventType.JOINED_ACTIVITY)
                .build();
    }

    @Bean
    public MailTemplateImpl getMailResetPassword() {
        return MailTemplateImpl
                .builder()
                .subject("Account password reset")
                .message("Your account password has just been reset. " +
                        "If this was not you, please contact us immediately.")
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
                .message("A user has joined one of the activities " +
                        "you are hosting!")
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
}
