package nl.tudelft.sem.project.activities.database.entities;


import nl.tudelft.sem.project.ConverterEntityDTO;
import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.activities.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.stream.Collectors;

@Service
public class CompetitionConverterService implements ConverterEntityDTO<CompetitionDTO, Competition> {

    @Autowired
    transient ActivityService activityService;
    @Autowired
    transient BoatConverterService boatConverterService;

    @Override
    public CompetitionDTO toDTO(Competition competition) {
        return new CompetitionDTO(
                competition.getId(),
                competition.getLocation(),
                competition.getOwner(),
                java.sql.Timestamp.valueOf(competition.getStartTime()),
                java.sql.Timestamp.valueOf(competition.getEndTime()),
                competition.getBoats().stream().map(boatConverterService::toDTO).collect(Collectors.toList()),
                competition.getAllowsAmateurs(),
                competition.getRequiredOrganization(),
                competition.getRequiredGender()
        );
    }

    @Override
    public Competition toEntity(CompetitionDTO dto) {
        return new Competition(
                dto.getId(),
                dto.getLocation(),
                dto.getOwner(),
                dto.getStartTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(),
                dto.getEndTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(),
                dto.getBoats().stream().map(boatConverterService::toEntity).collect(Collectors.toList()),
                dto.getAllowsAmateurs(),
                dto.getRequiredOrganization(),
                dto.getRequiredGender()
        );
    }

    @Override
    public Competition toDatabaseEntity(CompetitionDTO dto) {
        return activityService.getCompetitionById(dto.getId());
    }
}
