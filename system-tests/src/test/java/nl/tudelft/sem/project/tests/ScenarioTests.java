package nl.tudelft.sem.project.tests;

import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.gateway.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
                        nl.tudelft.sem.project.notifications.Application.class).run("--server.port=8086"),
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.matchmaking.Application.class).run("--server.port=8083")
        );
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
    void createActivityAndSomeoneRegistersTest() {
        var boat = addBoatToTheDatabase(
                        BoatDTO.builder()
                                .name("boat 1")
                                .availablePositions(List.of(BoatRole.Coach))
                                .coxCertificateId(UUID.randomUUID()).build());

        var createUserModel = CreateUserModel.builder()
                .username("tester1")
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

        trainingDTO = gatewayActivitiesClient.createTraining("Bearer " + userToken, trainingDTO);
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
    }
}