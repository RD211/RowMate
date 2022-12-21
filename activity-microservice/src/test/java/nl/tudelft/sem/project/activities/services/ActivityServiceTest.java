package nl.tudelft.sem.project.activities.services;

import nl.tudelft.sem.project.activities.ActivityNotFoundException;
import nl.tudelft.sem.project.activities.database.entities.Activity;
import nl.tudelft.sem.project.activities.database.entities.Competition;
import nl.tudelft.sem.project.activities.database.entities.Training;
import nl.tudelft.sem.project.activities.database.repository.ActivityRepository;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    @Test
    void findActivitiesFromFilter() {
        Date startDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        Date endDate = Date.from(LocalDateTime.now().plusMinutes(60).toInstant(ZoneOffset.UTC));
        //LocalDateTime start = LocalDateTime.of(2022, 10, 14, 17, 30);
        //LocalDateTime end = LocalDateTime.of(2022, 10, 14, 18, 30);
        LocalDateTime start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        List<Activity> ans = List.of(Activity.builder().id(UUID.randomUUID()).build());
        when(activityRepository.findWithinTimeSlot(start, end)).thenReturn(ans);
        var input = ActivityFilterDTO
                .builder()
                .startTime(startDate)
                .endTime(endDate)
                .build();
        var ret = activityService.findActivitiesFromFilter(input);
        assertThat(ret).isEqualTo(ans);
        verify(activityRepository, times(1)).findWithinTimeSlot(start, end);
    }

    @Test
    void getActivityById() {
        UUID actId = UUID.randomUUID();
        Activity ans = Activity.builder()
                .id(actId)
                .owner("Some Owner")
                .build();
        when(activityRepository.findById(actId)).thenReturn(Optional.of(ans));
        var ret = activityService.getActivityById(actId);
        assertThat(ret).isEqualTo(ans);
        verify(activityRepository, times(1)).findById(actId);
    }

    @Test
    void getTrainingByIdNormal() {
        UUID actId = UUID.randomUUID();
        Training ans = Training.builder()
                .id(actId)
                .owner("Some Owner")
                .build();
        when(activityRepository.findById(actId)).thenReturn(Optional.of(ans));
        var ret = activityService.getTrainingById(actId);
        assertThat(ret).isEqualTo(ans);
        verify(activityRepository, times(1)).findById(actId);
    }

    @Test
    void getTrainingByIdWrongActivityType() {
        UUID actId = UUID.randomUUID();
        Competition ans = Competition.builder()
                .id(actId)
                .owner("Some Owner")
                .build();
        when(activityRepository.findById(actId)).thenReturn(Optional.of(ans));
        assertThatExceptionOfType(ActivityNotFoundException.class).isThrownBy(() ->
                activityService.getTrainingById(actId));
        verify(activityRepository, times(1)).findById(actId);
    }

    @Test
    void getCompetitionByIdNormal() {
        UUID actId = UUID.randomUUID();
        Competition ans = Competition.builder()
                .id(actId)
                .owner("Some Owner")
                .build();
        when(activityRepository.findById(actId)).thenReturn(Optional.of(ans));
        var ret = activityService.getCompetitionById(actId);
        assertThat(ret).isEqualTo(ans);
        verify(activityRepository, times(1)).findById(actId);
    }

    @Test
    void getCompetitionByIdWrongActivityType() {
        UUID actId = UUID.randomUUID();
        Training ans = Training.builder()
                .id(actId)
                .owner("Some Owner")
                .build();
        when(activityRepository.findById(actId)).thenReturn(Optional.of(ans));
        assertThatExceptionOfType(ActivityNotFoundException.class).isThrownBy(() ->
                activityService.getCompetitionById(actId));
        verify(activityRepository, times(1)).findById(actId);
    }

    @Test
    void addTraining() {
        Training training = Training.builder()
                .id(UUID.randomUUID())
                .owner("New Activity Owner")
                .build();
        when(activityRepository.save(training)).thenReturn(training);
        var ret = activityService.addTraining(training);
        assertThat(ret).isEqualTo(training);
        verify(activityRepository, times(1)).save(training);
    }

    @Test
    void addCompetition() {
        Competition competition = Competition.builder()
                .id(UUID.randomUUID())
                .owner("New Activity Owner")
                .build();
        when(activityRepository.save(competition)).thenReturn(competition);
        var ret = activityService.addCompetition(competition);
        assertThat(ret).isEqualTo(competition);
        verify(activityRepository, times(1)).save(competition);
    }
}