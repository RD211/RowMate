package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.activities.*;
import nl.tudelft.sem.project.gateway.CreateCompetitionModel;
import nl.tudelft.sem.project.gateway.CreateTrainingModel;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
}