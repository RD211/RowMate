package nl.tudelft.sem.project.notifications.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailSenderConfig {

    @Value("${application.properties.test-mode:false}")
    private transient String testMode;

    /**
     * Bean returning a configured mail sender.
     *
     * @return JavaMailSender
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        System.out.println(testMode + " for mail");
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String trueLiteral = "true";
        if (!testMode.equals("true")) {
            mailSender.setHost("smtp.gmail.com");
            mailSender.setPort(587);

            mailSender.setUsername("noreply.rowing.delft@gmail.com");
            mailSender.setPassword("fcqtibzdpmwycumf");

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", trueLiteral);
            props.put("mail.smtp.starttls.enable", trueLiteral);
            props.put("mail.debug", trueLiteral);
        } else {
            mailSender.setHost("localhost");
            mailSender.setPort(587);

            mailSender.setUsername("localhost");

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "false");
            props.put("mail.smtp.starttls.enable", "false");
            props.put("mail.debug", trueLiteral);
        }
        return mailSender;
    }
}
