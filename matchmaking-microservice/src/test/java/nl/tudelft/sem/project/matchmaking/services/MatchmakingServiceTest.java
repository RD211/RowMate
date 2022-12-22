package nl.tudelft.sem.project.matchmaking.services;

import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistrationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MatchmakingServiceTest {

    @Mock
    ActivityRegistrationRepository activityRegistrationRepository;

    @InjectMocks
    MatchmakingService matchmakingService;

    @Test
    void deleteUserFromActivity() {
        UUID activityId = UUID.randomUUID();
        matchmakingService.deleteUserFromActivity("User", activityId);
        verify(activityRegistrationRepository, times(1))
                .deleteByUserNameAndActivityId("User", activityId);
    }
}