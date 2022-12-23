package nl.tudelft.sem.project.activities.database.entities;

import nl.tudelft.sem.project.ConverterEntityDTO;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.activities.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Service
public class ActivityConverterService implements ConverterEntityDTO<ActivityDTO, Activity> {

    @Autowired
    transient ActivityService activityService;

    @Autowired
    transient BoatConverterService boatConverterService;

    @Override
    public ActivityDTO toDTO(Activity activity) {
        if (activity instanceof Training) {
            Training training = (Training) activity;
            return new TrainingDTO(training.getId(),
                    training.getLocation(),
                    training.getOwner(),
                    java.sql.Timestamp.valueOf(training.getStartTime()),
                    java.sql.Timestamp.valueOf(training.getEndTime()),
                    training.getBoats().stream().map(boatConverterService::toDTO).collect(Collectors.toList()));
        } else if (activity instanceof Competition) {
            Competition competition = (Competition) activity;
            return new CompetitionDTO(activity.getId(),
                    competition.getLocation(),
                    competition.getOwner(),
                    java.sql.Timestamp.valueOf(competition.getStartTime()),
                    java.sql.Timestamp.valueOf(competition.getEndTime()),
                    competition.getBoats().stream().map(boatConverterService::toDTO).collect(Collectors.toList()),
                    competition.getAllowsAmateurs(),
                    competition.getRequiredOrganization(),
                    competition.getRequiredGender());
        }
        return ActivityDTO.builder()
                .id(activity.getId())
                .boats(activity.getBoats().stream().map(boatConverterService::toDTO).collect(Collectors.toList()))
                .owner(activity.getOwner())
                .startTime(java.sql.Timestamp.valueOf(activity.getStartTime()))
                .endTime(java.sql.Timestamp.valueOf(activity.getEndTime()))
                .location(activity.getLocation()).build();
    }

    @Override
    public Activity toEntity(ActivityDTO dto) {
        return Activity.builder()
                .id(dto.getId())
                .boats(dto.getBoats().stream().map(boatConverterService::toEntity).collect(Collectors.toList()))
                .owner(dto.getOwner())
                .startTime(dto.getStartTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .endTime(dto.getEndTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .location(dto.getLocation()).build();
    }

    @Override
    public Activity toDatabaseEntity(ActivityDTO dto) {
        return activityService.getTrainingById(dto.getId());
    }
}