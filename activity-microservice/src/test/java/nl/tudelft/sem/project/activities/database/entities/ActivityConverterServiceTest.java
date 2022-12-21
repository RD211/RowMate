package nl.tudelft.sem.project.activities.database.entities;

import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.activities.services.ActivityService;
import nl.tudelft.sem.project.enums.BoatRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ActivityConverterServiceTest {


    @Mock
    ActivityService activityService;

    @Mock
    BoatConverterService boatConverterService;

    @InjectMocks
    ActivityConverterService activityConverterService;

    Activity entity;

    ActivityDTO dto;

    @BeforeEach
    void setup() {
        var boat = Boat.builder().availablePositions(List.of(BoatRole.Coach)).build();
        var boatDTO = BoatDTO.builder().availablePositions(List.of(BoatRole.Coach)).build();
        entity = new Activity(
                UUID.randomUUID(),
                "",
                "",
                LocalDateTime.of(2030, 1, 1, 1, 1, 1),
                LocalDateTime.of(2030, 1, 2, 1, 1, 1),
                List.of(boat)
        );
        dto = new ActivityDTO(
                entity.getId(),
                "",
                "",
                java.sql.Timestamp.valueOf(entity.getStartTime()),
                java.sql.Timestamp.valueOf(entity.getEndTime()),
                List.of(boatDTO)
        );

        when(boatConverterService.toDTO(boat)).thenReturn(boatDTO);
        when(boatConverterService.toEntity(boatDTO)).thenReturn(boat);

    }

    @Test
    void toDTO() {
        assertEquals(
                dto,
                activityConverterService.toDTO(entity)
        );
    }

    @Test
    void toEntity() {
        assertEquals(
                entity,
                activityConverterService.toEntity(dto)
        );
    }

    @Test
    void toDatabaseEntity() {
        when(activityService.getActivityById(entity.getId())).thenReturn(entity);
        assertEquals(
                entity,
                activityConverterService.toEntity(dto)
        );
    }
}