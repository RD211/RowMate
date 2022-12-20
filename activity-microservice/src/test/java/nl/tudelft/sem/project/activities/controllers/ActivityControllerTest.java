package nl.tudelft.sem.project.activities.controllers;

import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.activities.database.entities.*;
import nl.tudelft.sem.project.activities.services.ActivityService;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ActivityControllerTest {

    @Mock
    transient ActivityService activityService;

    @Mock
    transient TrainingConverterService trainingConverterService;

    @Mock
    transient CompetitionConverterService competitionConverterService;

    @Mock
    transient ActivityConverterService activityConverterService;

    @InjectMocks
    ActivityController activityController;

    @Test
    void findActivitiesFromFilter() {
        var filter = new ActivityFilterDTO(
                Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now().plusDays(1)),
                List.of(BoatRole.Coach)
        );
        var activity = new Activity(
                null,
                "",
                "",
                LocalDateTime.of(2030, 1, 1, 1, 1, 1),
                LocalDateTime.of(2030, 1, 2, 1, 1, 1),
                List.of(Boat.builder().availablePositions(List.of(BoatRole.Coach)).build())
        );

        var activityDTO = new ActivityDTO(
                null,
                "",
                "",
                java.sql.Timestamp.valueOf(activity.getStartTime()),
                java.sql.Timestamp.valueOf(activity.getEndTime()),
                List.of(BoatDTO.builder().availablePositions(List.of(BoatRole.Coach)).build())
        );

        when(activityService.findActivitiesFromFilter(filter)).thenReturn(
                List.of(activity)
        );

        when(activityConverterService.toDTO(activity)).thenReturn(activityDTO);
        var result = activityController.findActivitiesFromFilter(filter).getBody();

        assertEquals(List.of(activityDTO), result);
    }

    @Test
    void createTraining() {
        var training = new Training(
                UUID.randomUUID(),
                "",
                "",
                LocalDateTime.of(2030, 1, 1, 1, 1, 1),
                LocalDateTime.of(2030, 1, 2, 1, 1, 1),
                List.of(Boat.builder().availablePositions(List.of(BoatRole.Coach)).build())
        );
        var trainingDTO = new TrainingDTO(
                null,
                "",
                "",
                java.sql.Timestamp.valueOf(training.getStartTime()),
                java.sql.Timestamp.valueOf(training.getEndTime()),
                List.of(BoatDTO.builder().availablePositions(List.of(BoatRole.Coach)).build())
        );

        when(trainingConverterService.toEntity(trainingDTO)).thenReturn(training);
        when(activityService.addTraining(training)).thenReturn(training);
        when(trainingConverterService.toDTO(training)).thenReturn(trainingDTO);

        var response = activityController.createTraining(trainingDTO).getBody();
        verify(activityService, times(1)).addTraining(training);
        assertEquals(trainingDTO, response);
    }

    @Test
    void createCompetition() {
        var competition = new Competition(
                UUID.randomUUID(),
                "",
                "",
                LocalDateTime.of(2030, 1, 1, 1, 1, 1),
                LocalDateTime.of(2030, 1, 2, 1, 1, 1),
                List.of(Boat.builder().availablePositions(List.of(BoatRole.Coach)).build()),
                false,
                null,
                null
        );
        var competitionDTO = new CompetitionDTO(
                null,
                "",
                "",
                java.sql.Timestamp.valueOf(competition.getStartTime()),
                java.sql.Timestamp.valueOf(competition.getEndTime()),
                List.of(BoatDTO.builder().availablePositions(List.of(BoatRole.Coach)).build()),
                false,
                null,
                null
        );

        when(competitionConverterService.toEntity(competitionDTO)).thenReturn(competition);
        when(activityService.addCompetition(competition)).thenReturn(competition);
        when(competitionConverterService.toDTO(competition)).thenReturn(competitionDTO);

        var response = activityController.createCompetition(competitionDTO).getBody();
        verify(activityService, times(1)).addCompetition(competition);
        assertEquals(competitionDTO, response);
    }

    @Test
    void getCompetitionById() {
        var competition = new Competition(
                UUID.randomUUID(),
                "",
                "",
                LocalDateTime.of(2030, 1, 1, 1, 1, 1),
                LocalDateTime.of(2030, 1, 2, 1, 1, 1),
                List.of(Boat.builder().availablePositions(List.of(BoatRole.Coach)).build()),
                false,
                null,
                null
        );
        var competitionDTO = new CompetitionDTO(
                competition.getId(),
                "",
                "",
                java.sql.Timestamp.valueOf(competition.getStartTime()),
                java.sql.Timestamp.valueOf(competition.getEndTime()),
                List.of(BoatDTO.builder().availablePositions(List.of(BoatRole.Coach)).build()),
                false,
                null,
                null
        );

        when(activityService.getCompetitionById(competition.getId())).thenReturn(competition);
        when(competitionConverterService.toDTO(competition)).thenReturn(competitionDTO);

        var result = activityController.getCompetitionById(competition.getId()).getBody();

        verify(activityService, times(1)).getCompetitionById(competition.getId());
        assertEquals(competitionDTO, result);
    }

    @Test
    void getTrainingById() {
        var training = new Training(
                UUID.randomUUID(),
                "",
                "",
                LocalDateTime.of(2030, 1, 1, 1, 1, 1),
                LocalDateTime.of(2030, 1, 2, 1, 1, 1),
                List.of(Boat.builder().availablePositions(List.of(BoatRole.Coach)).build())
        );
        var trainingDTO = new TrainingDTO(
                training.getId(),
                "",
                "",
                java.sql.Timestamp.valueOf(training.getStartTime()),
                java.sql.Timestamp.valueOf(training.getEndTime()),
                List.of(BoatDTO.builder().availablePositions(List.of(BoatRole.Coach)).build())
        );

        when(activityService.getTrainingById(training.getId())).thenReturn(training);
        when(trainingConverterService.toDTO(training)).thenReturn(trainingDTO);

        var result = activityController.getTrainingById(training.getId()).getBody();

        verify(activityService, times(1)).getTrainingById(training.getId());
        assertEquals(trainingDTO, result);
    }

    @Test
    void getActivityById() {
        var training = new Training(
                UUID.randomUUID(),
                "",
                "",
                LocalDateTime.of(2030, 1, 1, 1, 1, 1),
                LocalDateTime.of(2030, 1, 2, 1, 1, 1),
                List.of(Boat.builder().availablePositions(List.of(BoatRole.Coach)).build())
        );
        var trainingDTO = new TrainingDTO(
                training.getId(),
                "",
                "",
                java.sql.Timestamp.valueOf(training.getStartTime()),
                java.sql.Timestamp.valueOf(training.getEndTime()),
                List.of(BoatDTO.builder().availablePositions(List.of(BoatRole.Coach)).build())
        );

        when(activityService.getActivityById(training.getId())).thenReturn(training);
        when(activityConverterService.toDTO(training)).thenReturn(trainingDTO);

        var result = activityController.getActivityById(training.getId()).getBody();

        verify(activityService, times(1)).getActivityById(training.getId());
        assertEquals(trainingDTO, result);
    }
}