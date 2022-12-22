package nl.tudelft.sem.project.matchmaking.controllers;

import nl.tudelft.sem.project.matchmaking.services.MatchmakingService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
}