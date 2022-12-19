package nl.tudelft.sem.project.activities.database.entities;


import nl.tudelft.sem.project.ConverterEntityDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.activities.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingConverterService implements ConverterEntityDTO<TrainingDTO, Training>  {

    @Autowired
    transient ActivityService activityService;

    @Autowired
    transient BoatConverterService boatConverterService;

    @Override
    public TrainingDTO toDTO(Training training) {
        return new TrainingDTO(
                training.getId(),
                training.getLocation(),
                training.getOwner(),
                java.sql.Timestamp.valueOf(training.getStartTime()),
                java.sql.Timestamp.valueOf(training.getEndTime()),
                training.getBoats().stream().map(boatConverterService::toDTO).collect(Collectors.toList())
        );
    }

    @Override
    public Training toEntity(TrainingDTO dto) {
        return new Training(
                dto.getId(),
                dto.getLocation(),
                dto.getOwner(),
                dto.getStartTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(),
                dto.getEndTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(),
                dto.getBoats().stream().map(boatConverterService::toEntity).collect(Collectors.toList())
        );
    }

    @Override
    public Training toDatabaseEntity(TrainingDTO dto) {
        return activityService.getTrainingById(dto.getId());
    }
}
