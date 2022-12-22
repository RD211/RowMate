package nl.tudelft.sem.project.notifications.services;

import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.NotificationDTO;
import nl.tudelft.sem.project.notifications.exceptions.MailNotSentException;
import nl.tudelft.sem.project.users.UserDTO;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("dev")
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
public class NotificationsServiceImplTestExceptions {

    @Autowired
    private NotificationsServiceImpl notificationsService;

    @Test
    public void testInvalidHost() {
        JavaMailSender sender = notificationsService.getMailSender();
        ((JavaMailSenderImpl) notificationsService.getMailSender()).setHost("invalid.host.lmao");

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .userDTO(UserDTO.builder()
                        .email("TEST@TEST.TEST")
                        .username("TEEEEEEEEEEST!!!!")
                        .build())
                .eventType(EventType.TEST)
                .activityDTO(new TrainingDTO())
                .build();

        assertThrows(MailNotSentException.class, () -> notificationsService.sendNotification(notificationDTO));
    }
}