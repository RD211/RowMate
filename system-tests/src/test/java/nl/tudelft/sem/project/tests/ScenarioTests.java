package nl.tudelft.sem.project.tests;

import feign.FeignException;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.gateway.*;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import nl.tudelft.sem.project.matchmaking.ActivityRegistrationResponseDTO;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.UserDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes=nl.tudelft.sem.project.system.tests.Application.class)
public class ScenarioTests {


    @Autowired
    GatewayAuthenticationClient gatewayAuthenticationClient;

    @Autowired
    GatewayUserClient gatewayUserClient;

    @Autowired
    GatewayAdminClient gatewayAdminClient;

    @Autowired
    GatewayBoatsClient gatewayBoatsClient;

    @Autowired
    GatewayActivitiesClient gatewayActivitiesClient;

    @Autowired
    GatewayMatchmakingClient gatewayMatchmakingClient;

    @Autowired
    GatewayCertificatesClient gatewayCertificatesClient;
    static List<ConfigurableApplicationContext> microservices;

    @BeforeAll
    static void startEverything() {
        microservices = List.of(
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.users.Application.class).run("--server.port=8084"),
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.gateway.Application.class).properties("jwt.secret=exampleSecret").run("--server.port=8087"),
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.authentication.Application.class).properties("jwt.secret=exampleSecret").run("--server.port=8081"),
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.activities.Application.class).run("--server.port=8085"),
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.notifications.Application.class).properties("application.properties.test-mode=true").run("--server.port=8086"),
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.matchmaking.Application.class).run("--server.port=8083")
        );
    }

    @AfterAll
    static void shutdownEverything() {
        microservices.forEach(x -> {
            x.stop();
            x.close();
        });
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

    @Test
    void createActivityAndSomeoneRegistersTest() {
        var newCertificate = new CertificateDTO(
          null,
          "best cert",
                null
        );

        newCertificate = addCertificateToTheDatabase(newCertificate);
        var boat = addBoatToTheDatabase(
                        BoatDTO.builder()
                                .name("boat 1")
                                .availablePositions(List.of(BoatRole.Coach))
                                .coxCertificateId(newCertificate.getId()).build());

        var createUserModel = CreateUserModel.builder()
                .username("tester1")
                .email("tester@test.test")
                .password("testertester").build();
        var userToken = gatewayAuthenticationClient.register(
                createUserModel
        );

        var trainingModel =
                new CreateTrainingModel("idk",
                        new DateInterval(java.sql.Timestamp.valueOf(
                                LocalDateTime.of(2026, 11, 1, 1, 1, 1, 1)),
                        java.sql.Timestamp.valueOf(
                                LocalDateTime.of(2026, 12, 1, 1, 1, 1, 1))),
                        List.of(boat.getBoatId()));

        var trainingDTO = gatewayActivitiesClient.createTraining("Bearer " + userToken, trainingModel);
        var queriedDTO =
                gatewayActivitiesClient.getTraining("Bearer " + userToken, trainingDTO.getId());


        var otherUserToken = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username("tester2")
                        .email("tester2@test.test")
                        .password("testertester2").build()
        );

        var responseRegister = gatewayMatchmakingClient.registerInActivity(
                "Bearer " + otherUserToken,
                new SeatedUserModel(
                        queriedDTO.getId(),
                        0,
                        BoatRole.Coach
                )
        );

        var applications =
                gatewayMatchmakingClient.getWaitingApplications("Bearer " + otherUserToken);
        assertNotNull(applications);
        assertEquals(1, applications.size());


        assertThatThrownBy(() -> {
            gatewayMatchmakingClient.respondToRegistration(
                    "Bearer " + otherUserToken,
                    new ActivityRegistrationResponseDTO(
                            "tester2",
                            queriedDTO.getId(),
                            true
                    )
            );
        }).isInstanceOf(FeignException.class)
                        .hasMessageContaining("You are not the owner of this activity!");

        gatewayMatchmakingClient.respondToRegistration(
                "Bearer " + userToken,
                new ActivityRegistrationResponseDTO(
                        "tester2",
                        queriedDTO.getId(),
                        true
                )
        );

        applications = gatewayMatchmakingClient.getAcceptedApplications("Bearer " + otherUserToken);
        assertNotNull(applications);
        assertEquals(1, applications.size());
    }

    @Test
    void createActivityAndSomeoneUsesStrategy() {
        var boat = addBoatToTheDatabase(
                BoatDTO.builder()
                        .name("boat 1")
                        .availablePositions(List.of(BoatRole.Coach))
                        .coxCertificateId(UUID.randomUUID()).build());

        var createUserModel = CreateUserModel.builder()
                .username("userOne")
                .email("tester@test.test")
                .password("testertester").build();
        var userToken = gatewayAuthenticationClient.register(
                createUserModel
        );

        var trainingDTO =
                new TrainingDTO(null, "idk", createUserModel.getUsername(),
                        java.sql.Timestamp.valueOf(
                                LocalDateTime.of(2026, 11, 1, 1, 1, 1, 1)),
                        java.sql.Timestamp.valueOf(
                                LocalDateTime.of(2026, 12, 1, 1, 1, 1, 1)),
                        List.of(boat));

        gatewayActivitiesClient.createTraining("Bearer " + userToken, trainingDTO);

        var otherUserToken = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username("userTwo")
                        .email("tester2@test.test")
                        .password("testertester2").build()
        );

        var rsp = gatewayMatchmakingClient.autoFindActivity(
                "Bearer " + otherUserToken,
                MatchmakingStrategy.Random,
                new ActivityFilterDTO(
                    Date.from(LocalDateTime.of(2020, 11, 1, 1, 1, 1, 1).toInstant(ZoneOffset.UTC)),
                    Date.from(LocalDateTime.of(2030, 12, 1, 1, 1, 1, 1).toInstant(ZoneOffset.UTC)),
                    List.of(BoatRole.Coach)
                )
        );

        var applications =
                gatewayMatchmakingClient.getWaitingApplications("Bearer " + otherUserToken);
        assertNotNull(applications);
        assertEquals(1, applications.size());
    }
}