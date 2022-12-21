package nl.tudelft.sem.project.notifications.services;

import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.NotificationDTO;
import nl.tudelft.sem.project.users.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class NotificationsServiceImplTest {

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
        assertThat(res.getFrom()).isEqualTo("noreply.rowing.delft@gmail.com");
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
        assertThat(res.getFrom()).isEqualTo("noreply.rowing.delft@gmail.com");
        assertThat(res.getSubject()).isEqualTo("Test email");
        assertThat(res.getText()).contains("Test email.");
        assertThat(res.getText()).doesNotContain("Activity Details");
    }
}