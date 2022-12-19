package nl.tudelft.sem.project.activities.integration;

import nl.tudelft.sem.project.activities.*;
import nl.tudelft.sem.project.enums.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class FeignClientTest {

    @Autowired
    BoatsClient boatsClient;

    @Autowired
    ActivitiesClient activitiesClient;

    @Test
    public void createTrainingTest() {
        var boatDTO = BoatDTO.builder().name("good boat")
                .coxCertificateId(UUID.randomUUID())
                .availablePositions(List.of())
                .build();
        boatDTO = boatsClient.addBoat(boatDTO);

        assertEquals(boatDTO, boatsClient.getBoat(boatDTO.getBoatId()));

        var trainingDTO = new TrainingDTO(
                null,
                "bucharest",
                "tester",
                java.sql.Timestamp.valueOf(LocalDateTime.of(2022, 11, 1, 1, 1, 1, 1)),
                java.sql.Timestamp.valueOf(LocalDateTime.of(2022, 12, 1, 1, 1, 1, 1)),
                List.of(boatDTO)
        );
        trainingDTO = activitiesClient.createTraining(trainingDTO);
        var queriedCompetition = activitiesClient.getTraining(trainingDTO.getId());
        assertEquals(queriedCompetition, trainingDTO);
    }

    @Test
    public void createCompetitionTest() {
        var boatDTO = BoatDTO.builder().name("good boat2")
                .coxCertificateId(UUID.randomUUID())
                .availablePositions(List.of())
                .build();
        boatDTO = boatsClient.addBoat(boatDTO);

        assertEquals(boatDTO, boatsClient.getBoat(boatDTO.getBoatId()));

        var competitionDTO = new CompetitionDTO(
                null,
                "bucharest",
                "tester",
                java.sql.Timestamp.valueOf(LocalDateTime.of(2022, 11, 1, 1, 1, 1, 1)),
                java.sql.Timestamp.valueOf(LocalDateTime.of(2022, 12, 1, 1, 1, 1, 1)),
                List.of(boatDTO),
                false,
                null,
                Gender.Female
        );
        competitionDTO = activitiesClient.createCompetition(competitionDTO);
        var queriedCompetition = activitiesClient.getCompetition(competitionDTO.getId());
        assertEquals(queriedCompetition, competitionDTO);
    }

    @Test
    public void createCompetitionInvalidTest() {
        var boatDTO = BoatDTO.builder().name("good boat3")
                .coxCertificateId(UUID.randomUUID())
                .availablePositions(List.of())
                .build();
        boatDTO = boatsClient.addBoat(boatDTO);

        assertEquals(boatDTO, boatsClient.getBoat(boatDTO.getBoatId()));

        var competitionDTO = new CompetitionDTO(
                null,
                "bucharest",
                null,
                java.sql.Timestamp.valueOf(LocalDateTime.of(2022, 11, 1, 1, 1, 1, 1)),
                java.sql.Timestamp.valueOf(LocalDateTime.of(2022, 12, 1, 1, 1, 1, 1)),
                List.of(boatDTO),
                false,
                null,
                Gender.Male
        );
        assertThrows(Exception.class, () -> activitiesClient.createCompetition(competitionDTO));
    }
}
