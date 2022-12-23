package nl.tudelft.sem.project.matchmaking.services;

import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistration;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistrationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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

    @Test
    void getAllApplicationsToActivityByAcceptedStatus() {
        when(activityRegistrationRepository.findByActivityIdAndAccepted(any(UUID.class),
                any(boolean.class))).thenReturn(
                        List.of(new ActivityRegistration(
                                "userrr",
                                UUID.randomUUID(),
                                0,
                                BoatRole.Coach,
                                false)));
        var result =
                matchmakingService.getAllApplicationsToActivityByAcceptedStatus(UUID.randomUUID(), false);

        assertEquals(1, result.size());
    }
}