package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.activities.ActivitiesClient;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.gateway.SeatedUserModel;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
import nl.tudelft.sem.project.matchmaking.*;
import nl.tudelft.sem.project.notifications.NotificationsClient;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ActivityRegistrationControllerTest {

    @Mock
    private transient AuthManager authManager;
    @Mock
    private transient UsersClient usersClient;
    @Mock
    private transient MatchmakingClient matchmakingClient;

    @Mock
    transient NotificationsClient notificationClient;

    @Mock
    private transient ActivitiesClient activitiesClient;

    @InjectMocks
    ActivityRegistrationController activityRegistrationController;

    @Test
    void deleteByUserNameAndActivityId() {
        UUID activityId = UUID.randomUUID();
        ActivityDTO activityDTO = ActivityDTO.builder()
                .id(activityId)
                .owner("Owner")
                .build();

        when(authManager.getUsername()).thenReturn("Owner");
        when(activitiesClient.getActivity(activityId)).thenReturn(activityDTO);
        var ret = activityRegistrationController.deleteByUserNameAndActivityId(activityId, "User");
        verify(authManager, times(1)).getUsername();
        verify(activitiesClient, times(1)).getActivity(activityId);
        verify(matchmakingClient, times(1)).deleteByUserNameAndActivityId(activityId, "User");
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteByUserNameAndActivityIdNotOwner() {
        UUID activityId = UUID.randomUUID();
        ActivityDTO activityDTO = ActivityDTO.builder()
                .id(activityId)
                .owner("Owner")
                .build();

        when(authManager.getUsername()).thenReturn("Not Owner");
        when(activitiesClient.getActivity(activityId)).thenReturn(activityDTO);
        assertThrows(RuntimeException.class,
                () -> activityRegistrationController.deleteByUserNameAndActivityId(activityId, "User"));

        verify(authManager, times(1)).getUsername();
        verify(activitiesClient, times(1)).getActivity(activityId);
        verify(matchmakingClient, never()).deleteByUserNameAndActivityId(activityId, "User");
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
        assertThat(activityRegistrationController.deregister(UUID.randomUUID()))
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
        assertThat(activityRegistrationController.register(SeatedUserModel.builder().build()))
                .isEqualTo(ResponseEntity.ok("Successful!"));
    }

    @Test
    public void testRespond() {
        assertThatThrownBy(() -> {
            activityRegistrationController.respondToRegistration(ActivityRegistrationResponseDTO.builder().build());
        }).isInstanceOf(Exception.class);
    }

    @Test
    public void testAcceptedApplications() {
        when(matchmakingClient.getAcceptedApplications(any(String.class))).thenReturn(new ArrayList<>());
        assertThat(activityRegistrationController.getAcceptedApplications())
                .isEqualTo(ResponseEntity.ok(new ArrayList<>()));
    }

    @Test
    public void testWaitingApplications() {
        when(matchmakingClient.getWaitingApplications(any(String.class))).thenReturn(new ArrayList<>());
        assertThat(activityRegistrationController.getWaitingApplications())
                .isEqualTo(ResponseEntity.ok(new ArrayList<>()));
    }

    @Test
    void getParticipants() {
        var id = UUID.randomUUID();

        when(matchmakingClient.getApplicationsForActivityByStatus(
                id, true)).thenReturn(
                List.of(new ActivityApplicationModel("tester", 0, BoatRole.Coach))
        );

        var result =
                activityRegistrationController.getParticipants(
                        id
                ).getBody();

        assert result != null;
        assertEquals(1, result.size());
        verify(matchmakingClient, times(1)).getApplicationsForActivityByStatus(
                id, true);

    }

    @Test
    void getWaitingRoom() {
        var id = UUID.randomUUID();

        when(matchmakingClient.getApplicationsForActivityByStatus(
                id, false)).thenReturn(
                List.of(new ActivityApplicationModel("tester", 0, BoatRole.Coach))
        );

        var result =
                activityRegistrationController.getWaitingRoom(
                        id
                ).getBody();

        assert result != null;
        assertEquals(1, result.size());
        verify(matchmakingClient, times(1)).getApplicationsForActivityByStatus(
                id, false);
    }
}