package nl.tudelft.sem.project.matchmaking.controllers;

import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.matchmaking.ActivityApplicationModel;
import nl.tudelft.sem.project.matchmaking.services.MatchmakingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MatchmakingControllerTest {

    @Mock
    MatchmakingService matchmakingService;

    @InjectMocks
    MatchmakingController matchmakingController;

    @Test
    void deleteByUserNameAndActivityId() {
        UUID activityId = UUID.randomUUID();
        var ret = matchmakingController
                .deleteByUserNameAndActivityId(activityId, "username");
        verify(matchmakingService, times(1)).deleteUserFromActivity("username", activityId);
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getApplicationsForActivityByStatus() {
        var id = UUID.randomUUID();

        when(matchmakingService.getAllApplicationsToActivityByAcceptedStatus(
                id, false)).thenReturn(
                        List.of(new ActivityApplicationModel("tester",0, BoatRole.Coach))
        );

        var result =
                matchmakingController.getApplicationsForActivityByStatus(
                id,
                false
        ).getBody();

        assert result != null;
        assertEquals(1, result.size());
        verify(matchmakingService, times(1))
                .getAllApplicationsToActivityByAcceptedStatus(
                        id,
                        false
                );

    }
}