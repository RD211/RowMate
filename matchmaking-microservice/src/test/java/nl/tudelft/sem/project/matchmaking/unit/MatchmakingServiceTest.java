package nl.tudelft.sem.project.matchmaking.unit;

import nl.tudelft.sem.project.activities.ActivitiesClient;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.BoatsClient;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import nl.tudelft.sem.project.matchmaking.ActivityRequestDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistration;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistrationRepository;
import nl.tudelft.sem.project.matchmaking.services.MatchmakingService;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MatchmakingServiceTest {

    @Autowired
    private MatchmakingService matchmakingService;

    @MockBean
    private ActivitiesClient activitiesClient;

    @MockBean
    private BoatsClient boatsClient;
    @MockBean
    private ActivityRegistrationRepository activityRegistrationRepository;

    @MockBean
    private UsersClient usersClient;

    ActivityRequestDTO requestDTO;
    ActivityFilterDTO filterDTO;
    ActivityDTO activity;
    BoatDTO boat;

    protected UUID setUUID =  UUID.fromString("0000-00-00-00-000000");

    @BeforeEach
    void setUp() {
        filterDTO = ActivityFilterDTO
                    .builder()
                    .startTime(java.sql.Timestamp.valueOf(LocalDateTime.now()))
                    .endTime(java.sql.Timestamp.valueOf(LocalDateTime.now()))
                    .preferredRoles(List.of(BoatRole.Coach, BoatRole.Cox))
                    .build();

        requestDTO = ActivityRequestDTO
                    .builder()
                    .userName("testUser")
                    .activityFilter(filterDTO)
                    .build();

        activity =
                ActivityDTO
                    .builder()
                    .id(setUUID)
                    .location("somewhere")
                    .owner("someone")
                    .startTime(java.sql.Timestamp.valueOf(LocalDateTime.now()))
                    .endTime(java.sql.Timestamp.valueOf(LocalDateTime.now()))
                    .boats(List.of(BoatDTO.builder()
                            .boatId(setUUID)
                            .availablePositions(List.of(BoatRole.PortSideRower))
                            .build()))
                    .build();

        boat =
                BoatDTO
                    .builder()
                    .boatId(setUUID)
                    .name("Titanic")
                    .availablePositions(List.of(BoatRole.Coach))
                    .build();
    }

    @Test
    public void testAutoFindActivityEmptyList() {
        Mockito.when(activitiesClient.findActivitiesFromFilter(any(ActivityFilterDTO.class)))
                .thenReturn(List.of());

        assertThat(matchmakingService.autoFindActivity(MatchmakingStrategy.EarliestFirst, requestDTO))
                .isEqualTo(MatchmakingService.autoFindErrorMessage);
    }

    @Test
    public void testAutoFindFullActivity() {
        Mockito.when(activitiesClient.findActivitiesFromFilter(any(ActivityFilterDTO.class)))
                .thenReturn(List.of(activity));

        Mockito.when(activitiesClient.getActivity(activity.getId())).thenReturn(activity);

        Mockito.when(boatsClient.getBoat(any(UUID.class)))
                .thenReturn(boat);

        Mockito.when(usersClient.hasCertificate(any(), any())).thenReturn(true);

        ActivityRegistration registration =
                ActivityRegistration
                        .builder()
                        .userName("idk")
                        .activityId(setUUID)
                        .boat(0)
                        .role(BoatRole.Coach)
                        .build();

        Mockito.when(activityRegistrationRepository.findAllByActivityId(any(UUID.class)))
                .thenReturn(List.of(registration));

        assertThat(matchmakingService.autoFindActivity(MatchmakingStrategy.EarliestFirst, requestDTO))
                .isEqualTo(MatchmakingService.autoFindErrorMessage);
    }

    @Test
    public void testAutoFindNoPreferredRoleLeft() {
        Mockito.when(activitiesClient.findActivitiesFromFilter(any(ActivityFilterDTO.class)))
                .thenReturn(List.of(activity));

        Mockito.when(activitiesClient.getActivity(activity.getId())).thenReturn(activity);

        Mockito.when(usersClient.hasCertificate(any(), any())).thenReturn(true);

        boat.setAvailablePositions(List.of(BoatRole.Other));

        Mockito.when(boatsClient.getBoat(any(UUID.class)))
                .thenReturn(boat);

        Mockito.when(activityRegistrationRepository.findAllByActivityId(any(UUID.class)))
                .thenReturn(List.of());

        assertThat(matchmakingService.autoFindActivity(MatchmakingStrategy.EarliestFirst, requestDTO))
                .isEqualTo(MatchmakingService.autoFindErrorMessage);
    }
}
