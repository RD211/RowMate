package nl.tudelft.sem.project.matchmaking.unit;

import nl.tudelft.sem.project.activities.ActivitiesClient;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.BoatsClient;
import nl.tudelft.sem.project.matchmaking.*;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistration;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistrationId;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistrationRepository;
import nl.tudelft.sem.project.matchmaking.services.ActivityCheckerService;
import nl.tudelft.sem.project.matchmaking.services.MatchmakingService;
import nl.tudelft.sem.project.shared.Username;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

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
    ActivityCheckerService activityCheckerService;

    @MockBean
    private UsersClient usersClient;

    ActivityRequestDTO requestDTO;
    ActivityRegistrationRequestDTO registrationRequestDTO;
    ActivityDeregisterRequestDTO deregisterRequestDTO;
    ActivityRegistrationResponseDTO acceptResponseDTO;
    ActivityFilterDTO filterDTO;
    ActivityDTO activity;
    ActivityDTO activity2;
    BoatDTO boat;
    BoatDTO secondBoat;

    protected UUID setUUID =  UUID.fromString("0000-00-00-00-000000");
    protected UUID setUUID2 =  UUID.fromString("0000-00-00-00-000001");

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

        deregisterRequestDTO =
                ActivityDeregisterRequestDTO
                    .builder()
                    .userName("testUser")
                    .activityId(setUUID)
                    .build();

        acceptResponseDTO =
                ActivityRegistrationResponseDTO
                    .builder()
                    .activityId(setUUID)
                    .userName("testUser")
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

        activity2 =
                ActivityDTO
                    .builder()
                    .id(setUUID2)
                    .location("somewhere")
                    .owner("someone")
                    .startTime(java.sql.Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                    .endTime(java.sql.Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                    .boats(List.of(BoatDTO.builder()
                            .boatId(setUUID2)
                            .availablePositions(List.of(BoatRole.Coach))
                            .build()))
                    .build();


        boat =
                BoatDTO
                    .builder()
                    .boatId(setUUID)
                    .name("Titanic")
                    .availablePositions(List.of(BoatRole.Coach))
                    .build();

        secondBoat = BoatDTO
                .builder()
                .boatId(setUUID2)
                .name("Olympic")
                .availablePositions(List.of(BoatRole.Coach))
                .build();

        registrationRequestDTO =
                ActivityRegistrationRequestDTO
                        .builder()
                        .activityId(setUUID)
                        .userName("testUser")
                        .boat(0)
                        .boatRole(BoatRole.Coach)
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

    @Test
    public void findActivitiesTest() {
        Mockito.when(activitiesClient.findActivitiesFromFilter(any(ActivityFilterDTO.class)))
                .thenReturn(List.of(activity));

        assertThat(matchmakingService.findActivities(requestDTO)).isEqualTo(List.of(activity));
    }

    @Test
    public void testAllApplicationsByAccepted() {
        ActivityRegistration registration
            = ActivityRegistration.builder().boat(0).role(BoatRole.Cox).userName("Elon Musk").build();
        Mockito.when(activityRegistrationRepository.findByActivityIdAndAccepted(any(UUID.class), any(Boolean.class)))
                .thenReturn(List.of(registration));

        assertThat(matchmakingService.getAllApplicationsToActivityByAcceptedStatus(setUUID, true))
                .isEqualTo(List.of(new ActivityApplicationModel("Elon Musk", 0, BoatRole.Cox)));
    }

    @Test
    public void testRegisterNotAllowed() {
        Mockito.when(activitiesClient.getActivity(any(UUID.class)))
                .thenReturn(activity);

        Mockito.when(usersClient.getUserByUsername(any(Username.class)))
                .thenReturn(UserDTO.builder().username("testUser").build());
        Mockito.when(activityCheckerService.isAllowedToRegister(
                any(ActivityDTO.class), any(UserDTO.class), any(BoatRole.class), any(BoatDTO.class)
            )).thenReturn(false);

        assertThat(matchmakingService.registerUserInActivity(registrationRequestDTO))
                .isEqualTo(false);
    }

    @Test
    public void testRegisterOverlap() {
        Mockito.when(activitiesClient.getActivity(any(UUID.class)))
                .thenReturn(activity);

        Mockito.when(usersClient.getUserByUsername(any(Username.class)))
                .thenReturn(UserDTO.builder().username("testUser").build());
        Mockito.when(activityCheckerService.isAllowedToRegister(
                any(ActivityDTO.class),
                any(UserDTO.class),
                any(BoatRole.class),
                any(BoatDTO.class)))
            .thenReturn(true);

        Mockito.when(activityRegistrationRepository.findRequestOverlap(
                any(UUID.class), any(String.class), anyInt(), any(BoatRole.class)
            )).thenReturn(List.of(ActivityRegistration.builder().build()));

        assertThat(matchmakingService.registerUserInActivity(registrationRequestDTO))
                .isEqualTo(false);
    }

    @Test
    public void testRegisterAllowedNoOverlap() {
        Mockito.when(activitiesClient.getActivity(any(UUID.class)))
                .thenReturn(activity);

        Mockito.when(usersClient.getUserByUsername(any(Username.class)))
                .thenReturn(UserDTO.builder().username("testUser").build());
        Mockito.when(activityCheckerService.isAllowedToRegister(
                        any(ActivityDTO.class),
                        any(UserDTO.class),
                        any(BoatRole.class),
                        any(BoatDTO.class)))
                .thenReturn(true);

        Mockito.when(activityRegistrationRepository.findRequestOverlap(
            any(UUID.class), any(String.class), anyInt(), any(BoatRole.class)
            )).thenReturn(List.of());

        assertThat(matchmakingService.registerUserInActivity(registrationRequestDTO))
                .isEqualTo(true);
    }

    @Test
    public void testDeRegisterNotFound() {
        Mockito.when(
                activityRegistrationRepository.findById(any(ActivityRegistrationId.class))
            ).thenReturn(Optional.empty());

        assertThat(matchmakingService.deregisterUserFromActivity(deregisterRequestDTO))
                .isEqualTo(false);
    }

    @Test
    public void testDeRegisterFound() {
        Mockito.when(activityRegistrationRepository.findById(any(ActivityRegistrationId.class)))
            .thenReturn(Optional.of(
                ActivityRegistration.builder().activityId(setUUID).userName("testUser").build()
            ));

        assertThat(matchmakingService.deregisterUserFromActivity(deregisterRequestDTO))
                .isEqualTo(true);

        Mockito.verify(activityRegistrationRepository, Mockito.times(1))
                .delete(any(ActivityRegistration.class));
    }

    @Test
    public void testActivitiesUserIsPartOfEmpty() {
        Mockito.when(activityRegistrationRepository.findByUserNameAndAccepted(any(String.class), anyBoolean()))
                .thenReturn(List.of());

        assertThat(matchmakingService.getAllActivitiesThatUserIsPartOf("testUser"))
                .isEqualTo(List.of());
    }

    @Test
    public void testActivitiesUserIsPartOfNotEmpty() {
        Mockito.when(activitiesClient.getActivity(setUUID)).thenReturn(activity);
        Mockito.when(activityRegistrationRepository.findByUserNameAndAccepted(any(String.class), anyBoolean()))
                .thenReturn(List.of(
                        ActivityRegistration.builder().activityId(setUUID).boat(0).role(BoatRole.Cox).build()
                ));

        assertThat(matchmakingService.getAllActivitiesThatUserIsPartOf("testUser"))
                .isNotEmpty()
                .isNotEqualTo(null);
    }

    @Test
    public void testActivitiesUserAppliedToEmpty() {
        Mockito.when(activityRegistrationRepository.findByUserNameAndAccepted(any(String.class), anyBoolean()))
                .thenReturn(List.of());

        assertThat(matchmakingService.getAllActivitiesThatUserAppliedTo("testUser"))
                .isEqualTo(List.of());
    }

    @Test
    public void testActivitiesUserAppliedToNotEmpty() {
        Mockito.when(activitiesClient.getActivity(setUUID)).thenReturn(activity);
        Mockito.when(activityRegistrationRepository.findByUserNameAndAccepted(any(String.class), anyBoolean()))
                .thenReturn(List.of(ActivityRegistration.builder().activityId(setUUID).boat(0).role(BoatRole.Cox).build()));

        assertThat(matchmakingService.getAllActivitiesThatUserAppliedTo("testUser"))
                .isNotEmpty()
                .isNotEqualTo(null);
    }

    @Test
    public void testGenerateSuitableActivityTasks() {
        UserDTO userDTO = UserDTO.builder().username("testUser").build();
        Mockito.when(usersClient.getUserByUsername(any(Username.class))).thenReturn(userDTO);
        Mockito.when(activityCheckerService.isAllowedToParticipateInActivity(activity, userDTO)).thenReturn(false);

        assertThat(matchmakingService.generateSuitableActivityTasks(requestDTO, activity)).isEqualTo(List.of());
    }

    @Test
    public void testRespondToRegistrationEmpty() {
        Mockito.when(activityRegistrationRepository.findById(any(ActivityRegistrationId.class)))
                .thenReturn(Optional.empty());

        assertThat(matchmakingService.respondToRegistration(acceptResponseDTO)).isEqualTo(false);
    }

    @Test
    public void testRespondToRegistrationAlreadyAccepted() {
        Mockito.when(activityRegistrationRepository.findById(any(ActivityRegistrationId.class)))
                .thenReturn(Optional.of(ActivityRegistration.builder().accepted(true).build()));

        assertThat(matchmakingService.respondToRegistration(acceptResponseDTO)).isEqualTo(false);
    }

    @Test
    public void testRespondToRegistrationAccept() {
        acceptResponseDTO.setAccepted(true);
        Mockito.when(activityRegistrationRepository.findById(any(ActivityRegistrationId.class)))
                .thenReturn(Optional.of(ActivityRegistration.builder().accepted(false).build()));

        assertThat(matchmakingService.respondToRegistration(acceptResponseDTO)).isEqualTo(true);

        Mockito.verify(activityRegistrationRepository, Mockito.times(1))
                .save(any(ActivityRegistration.class));
    }

    @Test
    public void testRespondToRegistrationDeny() {
        Mockito.when(activityRegistrationRepository.findById(any(ActivityRegistrationId.class)))
                .thenReturn(Optional.of(ActivityRegistration.builder().accepted(false).build()));

        acceptResponseDTO.setAccepted(false);
        assertThat(matchmakingService.respondToRegistration(acceptResponseDTO)).isEqualTo(true);

        Mockito.verify(activityRegistrationRepository, Mockito.times(1)).delete(any(ActivityRegistration.class));
    }
}
