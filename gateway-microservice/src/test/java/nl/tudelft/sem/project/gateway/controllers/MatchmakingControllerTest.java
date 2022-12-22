package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.activities.ActivitiesClient;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.gateway.SeatedUserModel;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
import nl.tudelft.sem.project.matchmaking.*;
import nl.tudelft.sem.project.notifications.NotificationsClient;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    transient NotificationsClient notificationClient;

    @MockBean
    transient AuthManager authManager;

    @Autowired
    MatchmakingController matchmakingController;


    /**
     * Setup the test.
     */
    @BeforeAll
    public void mock() {
        when(authManager.getUsername()).thenReturn("testUser");

        when(activitiesClient.getActivity(any(UUID.class))).thenReturn(null);
    }

    @Test
    public void testList() {
        when(matchmakingClient.findActivities(any(ActivityRequestDTO.class))).thenReturn(new ArrayList<>());
        assertThat(matchmakingController.findActivities(ActivityFilterDTO.builder().build()))
            .isEqualTo(ResponseEntity.ok(new ArrayList<>()));
    }

    @Test
    public void testStrategy() {
        when(matchmakingClient.autoFindActivity(any(MatchmakingStrategy.class),
                any(ActivityRequestDTO.class))).thenReturn("Successful!");

        assertThat((matchmakingController.autoFindActivity(MatchmakingStrategy.Random,
                ActivityFilterDTO.builder().build())))
            .isEqualTo(ResponseEntity.ok("Successful!"));
    }

    @Test
    public void testDeregister() {
        when(usersClient.getUserByUsername(any())).thenReturn(UserDTO.builder().username("adsdas").email("dasads").build());
        when(activitiesClient.getActivity(any())).thenReturn(TrainingDTO.builder()
                .owner("aaads")
                .id(UUID.randomUUID())
                .build());
        when(matchmakingClient.deRegisterFromActivity(any(ActivityDeregisterRequestDTO.class)))
                .thenReturn("Successful!");
        assertThat(matchmakingController.deregister(UUID.randomUUID()))
                .isEqualTo(ResponseEntity.ok("Successful!"));
    }

    @Test
    public void testRegister() {
        when(usersClient.getUserByUsername(any())).thenReturn(UserDTO.builder().username("adsdas").email("dasads").build());
        when(activitiesClient.getActivity(any())).thenReturn(TrainingDTO.builder()
                .owner("aaads")
                .id(UUID.randomUUID())
                .build());
        when(authManager.getUsername()).thenReturn("adads");
        when(matchmakingClient.registerInActivity(any(ActivityRegistrationRequestDTO.class))).thenReturn("Successful!");
        assertThat(matchmakingController.register(SeatedUserModel.builder().build()))
                .isEqualTo(ResponseEntity.ok("Successful!"));
    }

    @Test
    public void testRespond() {
        assertThatThrownBy(() -> {
            matchmakingController.respondToRegistration(ActivityRegistrationResponseDTO.builder().build());
        }).isInstanceOf(Exception.class);
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
