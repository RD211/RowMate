package nl.tudelft.sem.project.activities.boundary;

import nl.tudelft.sem.project.activities.ActivitiesClient;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.BoatsClient;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.authentication.AuthClient;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import nl.tudelft.sem.project.users.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class CreateActivitiesBoundaryTests {

    @Autowired
    ActivitiesClient activitiesClient;

    @Autowired
    BoatsClient boatsClient;

    @Test
    void createTrainingInvalidLocationTest() {
        var boatDTO = BoatDTO.builder().name("a boat name")
                .coxCertificateId(UUID.randomUUID()).availablePositions(List.of(BoatRole.Coach)).build();

        boatsClient.addBoat(boatDTO);

        var dto = new TrainingDTO(
                null,
                "A".repeat(201),
                "a normal user",
                java.sql.Timestamp.valueOf(
                        LocalDateTime.of(2030, 1, 1, 1, 1, 1)),
                java.sql.Timestamp.valueOf(
                        LocalDateTime.of(2030, 2, 1, 1, 1, 1)),
                List.of(boatDTO)
        );
        assertThrows(Exception.class, () -> activitiesClient.createTraining(dto));
    }

    @Test
    void createTrainingValidLocationTest() {
        var boatDTO = BoatDTO.builder().name("another boat name")
                .coxCertificateId(UUID.randomUUID()).availablePositions(List.of(BoatRole.Coach)).build();

        boatDTO = boatsClient.addBoat(boatDTO);
        var dto = new TrainingDTO(
                null,
                "A".repeat(200),
                "a normal user",
                java.sql.Timestamp.valueOf(
                        LocalDateTime.of(2030, 1, 1, 1, 1, 1)),
                java.sql.Timestamp.valueOf(
                        LocalDateTime.of(2030, 2, 1, 1, 1, 1)),
                List.of(boatDTO)
        );
        var activity = activitiesClient.createTraining(dto);
    }
}
