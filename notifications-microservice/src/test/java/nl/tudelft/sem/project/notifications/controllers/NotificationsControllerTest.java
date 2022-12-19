package nl.tudelft.sem.project.notifications.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.NotificationDTO;
import nl.tudelft.sem.project.users.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
class NotificationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    private void initTests() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void sendMailTraining() throws Exception {
        TrainingDTO activityDTO = new TrainingDTO();
        UserDTO userDTO = UserDTO.builder()
                .username("testificate")
                .email("testificate@testific.ate")
                .build();
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .userDTO(userDTO)
                .activityDTO(activityDTO)
                .eventType(EventType.TEST)
                .build();
        String notificationJson = objectMapper.writeValueAsString(notificationDTO);

        ResultActions res = mockMvc.perform(post("/sendNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(notificationJson));

        res.andExpect(status().isOk());

        String response = res.andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo("Notification sent to testificate.");
    }

    @Test
    public void sendMailCompetition() throws Exception {
        CompetitionDTO activityDTO = new CompetitionDTO();
        UserDTO userDTO = UserDTO.builder()
                .username("testificate")
                .email("testificate@testific.ate")
                .build();
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .userDTO(userDTO)
                .activityDTO(activityDTO)
                .eventType(EventType.TEST)
                .build();
        String notificationJson = objectMapper.writeValueAsString(notificationDTO);

        ResultActions res = mockMvc.perform(post("/sendNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(notificationJson));

        res.andExpect(status().isOk());

        String response = res.andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo("Notification sent to testificate.");
    }

    @Test
    public void sendMailManual() throws Exception {
        ResultActions res = mockMvc.perform(post("/sendNotifManual")
                .contentType(MediaType.APPLICATION_JSON)
                .content("TEST@TEST.TEST"));

        res.andExpect(status().isOk());

        String response = res.andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo("Notification sent to TEST@TEST.TEST.");
    }
}