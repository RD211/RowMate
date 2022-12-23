package nl.tudelft.sem.project.notifications.services;

import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.NotificationDTO;
import nl.tudelft.sem.project.notifications.TestSMTP;
import nl.tudelft.sem.project.notifications.exceptions.MailNotSentException;
import nl.tudelft.sem.project.users.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Properties;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class NotificationsServiceImplTest extends TestSMTP {

    @Autowired
    private NotificationsServiceImpl notificationsService;

    @Test
    public void mailFormatTrainingTest() throws Exception {
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .userDTO(UserDTO.builder()
                        .email("TEST@TEST.TEST")
                        .username("TEEEEEEEEEEST!!!!")
                        .build())
                .eventType(EventType.TEST_ACTIVITY)
                .activityDTO(new TrainingDTO())
                .build();

        SimpleMailMessage res = notificationsService.sendNotification(notificationDTO);

        assertThat(res.getTo()[0]).isEqualTo("TEST@TEST.TEST");
        assertThat(res.getFrom()).isEqualTo("localhost");
        assertThat(res.getSubject()).isEqualTo("Test email");
        assertThat(res.getText()).contains("Test email.");
        assertThat(res.getText()).contains("Activity Details");
    }

    @Test
    public void mailFormatUserRelatedTest() throws Exception {
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .userDTO(UserDTO.builder()
                        .email("TEST@TEST.TEST")
                        .username("TEEEEEEEEEEST!!!!")
                        .build())
                .eventType(EventType.TEST)
                .activityDTO(new TrainingDTO())
                .build();

        SimpleMailMessage res = notificationsService.sendNotification(notificationDTO);

        assertThat(res.getTo()[0]).isEqualTo("TEST@TEST.TEST");
        assertThat(res.getFrom()).isEqualTo("localhost");
        assertThat(res.getSubject()).isEqualTo("Test email");
        assertThat(res.getText()).contains("Test email.");
        assertThat(res.getText()).doesNotContain("Activity Details");
    }

    @Test
    public void testMailSenderConfig() {
        JavaMailSender sender = notificationsService.getMailSender();
        JavaMailSenderImpl impl = (JavaMailSenderImpl) sender;

        assertThat(impl.getPort()).isEqualTo(587);
        assertThat(impl.getHost()).isEqualTo("localhost");
        assertThat(impl.getUsername()).isEqualTo("localhost");
    }
}