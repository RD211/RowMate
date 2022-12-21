package nl.tudelft.sem.project.tests.functional;

import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.gateway.*;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.users.CertificateDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes=nl.tudelft.sem.project.system.tests.Application.class)
public class ActivitiesFunctionalTests extends FunctionalTestsBase{


    public CertificateDTO addCertificateToTheDatabase(CertificateDTO dto) {
        var adminToken = gatewayAuthenticationClient.authenticate(
                AuthenticateUserModel.builder().username(
                        "administrator"
                ).password("administrator").build()
        );

        var certificateDTO = gatewayAdminClient.addCertificate("Bearer " + adminToken, dto);
        assertEquals(certificateDTO.getName(), dto.getName());
        assertEquals(certificateDTO.getSupersededId(), dto.getSupersededId());
        assertNotNull(certificateDTO.getId());
        return certificateDTO;
    }

    public BoatDTO addBoatToTheDatabase(BoatDTO dto) {
        var adminToken = gatewayAuthenticationClient.authenticate(
                AuthenticateUserModel.builder().username(
                        "administrator"
                ).password("administrator").build()
        );

        var boatDTO = gatewayAdminClient.addBoat("Bearer " + adminToken, dto);
        assertEquals(boatDTO.getName(), dto.getName());
        assertEquals(boatDTO.getAvailablePositions(), dto.getAvailablePositions());
        assertEquals(boatDTO.getCoxCertificateId(), dto.getCoxCertificateId());
        assertNotNull(boatDTO.getBoatId());
        return boatDTO;
    }

    @Test
    void createTrainingTest() {
        var newCertificate = new CertificateDTO(
                null,
                "best ce35rt",
                null
        );

        newCertificate = addCertificateToTheDatabase(newCertificate);

        var boat = addBoatToTheDatabase(
                BoatDTO.builder().name("bestest boat").availablePositions(
                                List.of(BoatRole.Coach)
                        ).coxCertificateId(newCertificate.getId())
                        .build()
        );
        var createUserModel = CreateUserModel.builder()
                .username("asdasdasda4sdas55d")
                .email("adasd13a44s@gdgafm54co.3om")
                .password("treyh4bd5tyr").build();
        var userToken = gatewayAuthenticationClient.register(
                createUserModel
        );

        var trainingModel =
                new CreateTrainingModel("idk",
                        new DateInterval(java.sql.Timestamp.valueOf(
                                LocalDateTime.of(2026, 12, 1, 1, 1, 1, 1)),
                                java.sql.Timestamp.valueOf(
                                        LocalDateTime.of(2026, 12, 1, 1, 1, 1, 1))),
                        List.of(boat.getBoatId()));

        var trainingDTO = gatewayActivitiesClient.createTraining("Bearer " + userToken, trainingModel);
        var queriedDTO =
                gatewayActivitiesClient.getTraining("Bearer " + userToken, trainingDTO.getId());
        assertNotNull(trainingDTO);
        assertEquals(queriedDTO, trainingDTO);
    }

    @Test
    void createCompetitionTest() {
        var newCertificate = new CertificateDTO(
                null,
                "best cert33",
                null
        );

        newCertificate = addCertificateToTheDatabase(newCertificate);

        var boat = addBoatToTheDatabase(
                BoatDTO.builder().name("bestest boat2").availablePositions(
                                List.of(BoatRole.Coach)
                        ).coxCertificateId(newCertificate.getId())
                        .build()
        );
        var createUserModel = CreateUserModel.builder()
                .username("asda3sdasda4sdas55d")
                .email("adasd133a44s@gdgafm54co.3om")
                .password("treyh34bd5tyr").build();
        var userToken = gatewayAuthenticationClient.register(
                createUserModel
        );

        var competitionModel =
                new CreateCompetitionModel("idk",
                        new DateInterval(java.sql.Timestamp.valueOf(
                                LocalDateTime.of(2026, 11, 1, 1, 1, 1, 1)),
                                java.sql.Timestamp.valueOf(
                                        LocalDateTime.of(2026, 12, 1, 1, 1, 1, 1))),
                        List.of(boat.getBoatId()),
                        true,
                        null,
                        null);

        var competitionDTO = gatewayActivitiesClient.createCompetition("Bearer " + userToken, competitionModel);
        var queriedDTO =
                gatewayActivitiesClient.getCompetition("Bearer " + userToken, competitionDTO.getId());
        assertNotNull(competitionDTO);
        assertEquals(queriedDTO, competitionDTO);
    }
}
