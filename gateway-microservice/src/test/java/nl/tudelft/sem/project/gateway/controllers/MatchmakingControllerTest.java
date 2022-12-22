package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.activities.ActivitiesClient;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import nl.tudelft.sem.project.matchmaking.ActivityRequestDTO;
import nl.tudelft.sem.project.matchmaking.MatchmakingClient;
import nl.tudelft.sem.project.notifications.NotificationClient;
import nl.tudelft.sem.project.users.UsersClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MatchmakingControllerTest {
    @MockBean
    transient MatchmakingClient matchmakingClient;

    @MockBean
    transient ActivitiesClient activitiesClient;

    @MockBean
    transient UsersClient usersClient;

    @MockBean
    transient NotificationClient notificationClient;

    @MockBean
    transient AuthManager authManager;

    @Autowired
    MatchmakingController matchmakingController;


    @BeforeAll
    public void mockAuth() {
        when(authManager.getUsername()).thenReturn("testUser");
    }

    @Test
    public void testList() {
        when(matchmakingClient.findActivities(any(ActivityRequestDTO.class))).thenReturn(new ArrayList<>());
        assertThat(matchmakingController.findActivities(ActivityFilterDTO.builder().build()))
            .isEqualTo(ResponseEntity.ok(new ArrayList<>()));
    }

    @Test
    public void testStrategy() {
        when(matchmakingClient.autoFindActivity(any(MatchmakingStrategy.class), any(ActivityRequestDTO.class))).thenReturn("Successful!");

        assertThat((matchmakingController.autoFindActivity(MatchmakingStrategy.Random, ActivityFilterDTO.builder().build())))
            .isEqualTo(ResponseEntity.ok("Successful!"));
    }

    @Test
    public void testAcceptedApplications() {
        when(matchmakingClient.getAcceptedApplications(any(String.class))).thenReturn(new ArrayList<>());
        assertThat(matchmakingController.getAcceptedApplications())
            .isEqualTo(ResponseEntity.ok(new ArrayList<>()));
    }

    @Test
    public void testWaitingApplications() {
        when(matchmakingClient.getWaitingApplications(any(String.class))).thenReturn(new ArrayList<>());
        assertThat(matchmakingController.getWaitingApplications())
                .isEqualTo(ResponseEntity.ok(new ArrayList<>()));
    }
}
