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
import nl.tudelft.sem.project.matchmaking.MatchmakingClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MatchmakingControllerTest {

    @Mock
    private transient AuthManager authManager;

    @Mock
    private transient MatchmakingClient matchmakingClient;

    @Mock
    transient NotificationsClient notificationClient;

    @Mock
    private transient ActivitiesClient activitiesClient;

    @InjectMocks
    MatchmakingController matchmakingController;

    @Test
    void deleteByUserNameAndActivityId() {
        UUID activityId = UUID.randomUUID();
        ActivityDTO activityDTO = ActivityDTO.builder()
                .id(activityId)
                .owner("Owner")
                .build();

        when(authManager.getUsername()).thenReturn("Owner");
        when(activitiesClient.getActivity(activityId)).thenReturn(activityDTO);
        var ret = matchmakingController.deleteByUserNameAndActivityId(activityId, "User");
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
        assertThrows(RuntimeException.class, () -> matchmakingController.deleteByUserNameAndActivityId(activityId, "User"));

        verify(authManager, times(1)).getUsername();
        verify(activitiesClient, times(1)).getActivity(activityId);
        verify(matchmakingClient, never()).deleteByUserNameAndActivityId(activityId, "User");
    }
}