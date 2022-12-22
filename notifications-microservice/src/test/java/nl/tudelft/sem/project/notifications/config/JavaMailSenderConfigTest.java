package nl.tudelft.sem.project.notifications.config;

import nl.tudelft.sem.project.notifications.TestSMTP;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
class JavaMailSenderConfigTest extends TestSMTP {

    @Autowired
    JavaMailSenderConfig config;

    @Test
    void getJavaMailSender() {
        JavaMailSender sender = config.getJavaMailSender();
        JavaMailSenderImpl senderImpl = (JavaMailSenderImpl) sender;

        assertThat(senderImpl.getPort()).isEqualTo(587);
        assertThat(senderImpl.getHost()).isEqualTo("smtp.gmail.com");
        assertThat(senderImpl.getUsername()).isEqualTo("noreply.rowing.delft@gmail.com");
    }
}