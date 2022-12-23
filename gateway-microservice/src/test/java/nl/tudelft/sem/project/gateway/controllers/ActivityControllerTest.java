package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.activities.*;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.gateway.CreateCompetitionModel;
import nl.tudelft.sem.project.gateway.CreateTrainingModel;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
import nl.tudelft.sem.project.matchmaking.ActivityApplicationModel;
import nl.tudelft.sem.project.matchmaking.MatchmakingClient;
import nl.tudelft.sem.project.notifications.NotificationsClient;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ActivityControllerTest {

    @Mock
    private transient AuthManager authManager;

    @Mock
    private transient UsersClient usersClient;

    @Mock
    private transient BoatsClient boatsClient;

    @Mock
    private transient NotificationsClient notificationsClient;

    @Mock
    private transient ActivitiesClient activitiesClient;

    @Mock
    private transient MatchmakingClient matchmakingClient;

    @InjectMocks
    ActivityController activityController;

    Date getDate(int year,  int month,  int day) {
        return java.sql.Timestamp.valueOf(
                LocalDateTime.of(year,  month,  day,  1,  1,  1,  1));
    }

    @Test
    void createTrainingValidTest() {
        var boatId = UUID.randomUUID();
        var boat = BoatDTO.builder().name("test boat").boatId(boatId).build();
        when(boatsClient.getBoat(boatId)).thenReturn(boat);
        var model = new CreateTrainingModel(
                "bucharest", 
                new DateInterval(
                        getDate(2026, 12, 1), 
                        getDate(2030, 1, 4)), 
                List.of(
                        boat.getBoatId()
                )
        );

        var withOwner = new TrainingDTO(
                null, 
                "bucharest", 
                "tester", 
                getDate(2026, 12, 1), 
                getDate(2030, 1, 4), 
                List.of(
                        boat
                )
        );

        var withId = new TrainingDTO(
                UUID.randomUUID(), 
                "bucharest", 
                "tester", 
                getDate(2026, 12, 1), 
                getDate(2030, 1, 4), 
                List.of(
                        boat
                )
        );
        when(authManager.getUsername()).thenReturn("tester");
        when(usersClient.getUserByUsername(new Username("tester"))).thenReturn(UserDTO.builder()
                .username("tester")
                .email("test@test.test")
                .build());
        when(activitiesClient.createTraining(withOwner)).thenReturn(
                withId
        );
        var response = activityController.createTraining(model).getBody();
        assert response != null;
        assertEquals(withId.getId(),  response.getId());
        assertEquals("tester",  response.getOwner());
    }

    @Test
    void createCompetitionValidTest() {
        var boatId = UUID.randomUUID();
        var boat = BoatDTO.builder().name("test boat").boatId(boatId).build();
        when(boatsClient.getBoat(boatId)).thenReturn(boat);
        var model = new CreateCompetitionModel(
                "tulcea",
                new DateInterval(
                        getDate(2026, 12, 1), 
                        getDate(2030, 1, 4)), 
                List.of(
                        boat.getBoatId()
                ), 
                null, 
                null, 
                null
        );

        var withOwner = new CompetitionDTO(
                null, 
                "tulcea",
                "tester", 
                getDate(2026, 12, 1), 
                getDate(2030, 1, 4), 
                List.of(
                        boat
                ), 
                null, 
                null, 
                null
        );

        var withId = new CompetitionDTO(
                UUID.randomUUID(), 
                "tulcea",
                "tester", 
                getDate(2026, 12, 1), 
                getDate(2030, 1, 4), 
                List.of(
                        boat
                ), 
                null, 
                null, 
                null
        );
        when(authManager.getUsername()).thenReturn("tester");
        when(usersClient.getUserByUsername(new Username("tester"))).thenReturn(UserDTO.builder()
                .username("tester")
                .email("test@test.test")
                .build());
        when(activitiesClient.createCompetition(withOwner)).thenReturn(
                withId
        );
        var response = activityController.createCompetition(model).getBody();
        assert response != null;
        assertEquals(withId.getId(),  response.getId());
        assertEquals("tester",  response.getOwner());
    }

    @Test
    void getCompetitionById() {
        var id = UUID.randomUUID();
        var competitionDTO = new CompetitionDTO(
                UUID.randomUUID(), 
                "tulcea",
                "tester", 
                getDate(2026, 12, 1), 
                getDate(2030, 1, 4), 
                List.of(), 
                true, 
                null, 
                null
        );
        when(activitiesClient.getCompetition(id)).thenReturn(
                competitionDTO
        );
        var result = activityController.getCompetitionById(id).getBody();
        assertEquals(competitionDTO,  result);
    }

    @Test
    void getTrainingById() {
        var id = UUID.randomUUID();
        var trainingDTO = new TrainingDTO(
                UUID.randomUUID(), 
                "bucharest", 
                "tester", 
                getDate(2026, 12, 1), 
                getDate(2030, 1, 4), 
                List.of()
        );
        when(activitiesClient.getTraining(id)).thenReturn(
                trainingDTO
        );
        var result = activityController.getTrainingById(id).getBody();
        assertEquals(trainingDTO,  result);
    }

    @Test
    void getActivityById() {
        var id = UUID.randomUUID();
        var activityDTO = new ActivityDTO(
                UUID.randomUUID(), 
                "bucharest", 
                "tester", 
                getDate(2026, 12, 1), 
                getDate(2030, 1, 4), 
                List.of()
        );
        when(activitiesClient.getActivity(id)).thenReturn(
                activityDTO
        );
        var result = activityController.getActivityById(id).getBody();
        assertEquals(activityDTO,  result);
    }

    @Test
    void addBoatToActivityNorml() {
        UUID activityId = UUID.randomUUID();
        ActivityDTO activityDTO = ActivityDTO.builder()
                .id(activityId)
                .owner("Owner")
                .build();
        UUID boatId = UUID.randomUUID();
        BoatDTO boatDTO = BoatDTO.builder()
                .boatId(boatId)
                .name("Boat")
                .build();

        ActivityDTO changedActivity = ActivityDTO.builder()
                .id(activityId)
                .owner("Owner")
                .boats(List.of(boatDTO))
                .build();
        when(authManager.getUsername()).thenReturn("Owner");
        when(activitiesClient.getActivity(activityId)).thenReturn(activityDTO);
        when(activitiesClient.addBoatToActivity(activityId, boatDTO)).thenReturn(changedActivity);
        var ret = activityController.addBoatToActivity(activityId, boatDTO);
        verify(authManager, times(1)).getUsername();
        verify(activitiesClient, times(1)).getActivity(activityId);
        verify(activitiesClient, times(1)).addBoatToActivity(activityId, boatDTO);
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ret.getBody()).isEqualTo(changedActivity);
    }

    @Test
    void addBoatToActivityNotOwner() {
        UUID activityId = UUID.randomUUID();
        ActivityDTO activityDTO = ActivityDTO.builder()
                .id(activityId)
                .owner("Owner")
                .build();
        UUID boatId = UUID.randomUUID();
        BoatDTO boatDTO = BoatDTO.builder()
                .boatId(boatId)
                .name("Boat")
                .build();

        ActivityDTO changedActivity = ActivityDTO.builder()
                .id(activityId)
                .owner("Owner")
                .boats(List.of(boatDTO))
                .build();
        when(authManager.getUsername()).thenReturn("Not Owner");
        when(activitiesClient.getActivity(activityId)).thenReturn(activityDTO);
        when(activitiesClient.addBoatToActivity(activityId, boatDTO)).thenReturn(changedActivity);

        assertThrows(RuntimeException.class, () -> {
            activityController.addBoatToActivity(activityId, boatDTO);
        });

        verify(activitiesClient, never()).addBoatToActivity(any(), any());
    }

    @Test
    void changeActivityTimesNormal() {
        Date startDate = getDate(2022, 10, 20);
        Date endDate = getDate(2022, 10, 21);
        UUID activityId = UUID.randomUUID();
        ActivityDTO activityDTO = ActivityDTO.builder()
                .id(activityId)
                .owner("Owner")
                .startTime(getDate(2022, 9, 20))
                .endTime(getDate(2022, 9, 21))
                .build();
        ChangeActivityTimeModel model = ChangeActivityTimeModel.builder()
                .activityId(activityId)
                .newStartDate(startDate)
                .newEndDate(endDate)
                .build();



        when(authManager.getUsername()).thenReturn("Owner");
        when(activitiesClient.getActivity(activityId)).thenReturn(activityDTO);
        when(activitiesClient.changeActivityTimes(model)).thenAnswer(i -> {
            var m = ((ChangeActivityTimeModel) i.getArguments()[0]);
            return ResponseEntity.ok(activityDTO.withStartTime(m.getNewStartDate()).withEndTime(m.getNewEndDate()));
        });
        var ret = activityController.changeActivityTimes(model);
        verify(authManager, times(1)).getUsername();
        verify(activitiesClient, times(1)).getActivity(activityId);
        verify(activitiesClient, times(1)).changeActivityTimes(model);
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ret.getBody().getId()).isEqualTo(activityId);
        assertThat(ret.getBody().getStartTime()).isEqualTo(startDate);
        assertThat(ret.getBody().getEndTime()).isEqualTo(endDate);
    }

    @Test
    void changeActivityTimeSlotNotOwner() {
        Date startDate = getDate(2022, 10, 20);
        Date endDate = getDate(2022, 10, 21);
        UUID activityId = UUID.randomUUID();
        ActivityDTO activityDTO = ActivityDTO.builder()
                .id(activityId)
                .owner("Owner")
                .startTime(getDate(2022, 9, 20))
                .endTime(getDate(2022, 9, 21))
                .build();
        ChangeActivityTimeModel model = ChangeActivityTimeModel.builder()
                .activityId(activityId)
                .newStartDate(startDate)
                .newEndDate(endDate)
                .build();



        when(authManager.getUsername()).thenReturn("Not Owner");
        when(activitiesClient.getActivity(activityId)).thenReturn(activityDTO);
        when(activitiesClient.changeActivityTimes(model)).thenAnswer(i -> {
            var m = ((ChangeActivityTimeModel) i.getArguments()[0]);
            return ResponseEntity.ok(activityDTO.withStartTime(m.getNewStartDate()).withEndTime(m.getNewEndDate()));
        });
        assertThrows(RuntimeException.class, () -> activityController.changeActivityTimes(model));
        verify(activitiesClient, never()).changeActivityTimes(any());
    }

    @Test
    void changeActivityTimeSlotInconsistentTimes() {
        Date startDate = getDate(2022, 10, 21);
        Date endDate = getDate(2022, 10, 20);
        UUID activityId = UUID.randomUUID();
        ActivityDTO activityDTO = ActivityDTO.builder()
                .id(activityId)
                .owner("Owner")
                .startTime(getDate(2022, 9, 20))
                .endTime(getDate(2022, 9, 21))
                .build();
        ChangeActivityTimeModel model = ChangeActivityTimeModel.builder()
                .activityId(activityId)
                .newStartDate(startDate)
                .newEndDate(endDate)
                .build();

        when(authManager.getUsername()).thenReturn("Owner");
        when(activitiesClient.getActivity(activityId)).thenReturn(activityDTO);
        when(activitiesClient.changeActivityTimes(model)).thenAnswer(i -> {
            var m = ((ChangeActivityTimeModel) i.getArguments()[0]);
            return ResponseEntity.ok(activityDTO.withStartTime(m.getNewStartDate()).withEndTime(m.getNewEndDate()));
        });
        assertThrows(RuntimeException.class, () -> activityController.changeActivityTimes(model));
        verify(activitiesClient, never()).changeActivityTimes(any());
    }

    @Test
    void getParticipants() {
        var id = UUID.randomUUID();

        when(matchmakingClient.getApplicationsForActivityByStatus(
                id, true)).thenReturn(
                List.of(new ActivityApplicationModel("tester", 0, BoatRole.Coach))
        );

        var result =
                activityController.getParticipants(
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
                activityController.getWaitingRoom(
                        id
                ).getBody();

        assert result != null;
        assertEquals(1, result.size());
        verify(matchmakingClient, times(1)).getApplicationsForActivityByStatus(
                id, false);
    }
}