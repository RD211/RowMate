package nl.tudelft.sem.project.notifications.config;

import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JavaMailSenderConfigTest {

    JavaMailSenderConfig config;

    @Test
    void getJavaMailSender() {
        config = new JavaMailSenderConfig();
        JavaMailSender sender = config.getJavaMailSender();
        JavaMailSenderImpl senderImpl = (JavaMailSenderImpl) sender;

        assertThat(senderImpl.getPort()).isEqualTo(587);
        assertThat(senderImpl.getHost()).isEqualTo("smtp.gmail.com");
        assertThat(senderImpl.getUsername()).isEqualTo("noreply.rowing.delft@gmail.com");
    }
}