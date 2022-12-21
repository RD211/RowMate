package nl.tudelft.sem.project.tests.functional;

import feign.FeignException;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.gateway.*;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import nl.tudelft.sem.project.matchmaking.ActivityRegistrationResponseDTO;
import nl.tudelft.sem.project.matchmaking.UserActivityApplication;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.users.CertificateDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes=nl.tudelft.sem.project.system.tests.Application.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MatchMakingFunctionalTests {
    @Autowired
    GatewayAuthenticationClient gatewayAuthenticationClient;

    @Autowired
    GatewayUserClient gatewayUserClient;

    @Autowired
    GatewayAdminClient gatewayAdminClient;

    @Autowired
    GatewayBoatsClient gatewayBoatsClient;

    @Autowired
    GatewayMatchmakingClient gatewayMatchmakingClient;

    @Autowired
    GatewayActivitiesClient gatewayActivitiesClient;

    static List<ConfigurableApplicationContext> microservices;

    String userToken;
    String otherUserToken;
    ActivityDTO queriedDTO;
    CertificateDTO certificateDTO;


    @BeforeAll
    void startEverything() {
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

        populateDb();
    }

    private void populateDb() {
        var createUserModel = CreateUserModel.builder()
                .username("tester1")
                .email("tester@test.test")
                .password("testertester").build();
        userToken = gatewayAuthenticationClient.register(
                createUserModel
        );
        otherUserToken =  gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username("tester2")
                        .email("tester2@test.test")
                        .password("testertester2")
                        .build()
        );

        gatewayUserClient.changeAmateur("Bearer " + userToken, true);

        var newCertificate = new CertificateDTO(
                null,
                "best cert",
                null
        );

        newCertificate = addCertificateToTheDatabase(newCertificate);
        certificateDTO = newCertificate;

        var boat = addBoatToTheDatabase(
                BoatDTO.builder()
                        .name("boat 1")
                        .availablePositions(List.of(BoatRole.Coach))
                        .coxCertificateId(newCertificate.getId()).build());

        var trainingModel =
                new CreateTrainingModel("idk",
                        new DateInterval(java.sql.Timestamp.valueOf(
                                LocalDateTime.of(2026, 11, 1, 1, 1, 1, 1)),
                                java.sql.Timestamp.valueOf(
                                        LocalDateTime.of(2026, 12, 1, 1, 1, 1, 1))),
                        List.of(boat.getBoatId()));

        var trainingDTO = gatewayActivitiesClient.createTraining("Bearer " + userToken, trainingModel);

        queriedDTO =
                gatewayActivitiesClient.getTraining("Bearer " + userToken, trainingDTO.getId());
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
        gatewayMatchmakingClient.registerInActivity(
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

        gatewayMatchmakingClient.deRegisterFromActivity("Bearer " + otherUserToken, queriedDTO.getId());
    }

    @Test
    void createActivityAndSomeoneUsesStrategy() {
        gatewayMatchmakingClient.autoFindActivity(
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

        gatewayMatchmakingClient.respondToRegistration(
                "Bearer " + userToken,
                new ActivityRegistrationResponseDTO(
                        "tester2",
                        queriedDTO.getId(),
                        false
                )
        );

        applications = gatewayMatchmakingClient.getAcceptedApplications("Bearer " + otherUserToken);
        assertEquals(0, applications.size());

        applications = gatewayMatchmakingClient.getWaitingApplications("Bearer " + otherUserToken);
        assertEquals(0, applications.size());
    }

    @Test
    void twoActivitiesAndSomeoneUsesEarliestFirst() {

        var boat = addBoatToTheDatabase(
                BoatDTO.builder()
                        .name("boat 2")
                        .availablePositions(List.of(BoatRole.Coach))
                        .coxCertificateId(certificateDTO.getId()).build());
        var trainingModel =
                new CreateTrainingModel("idk",
                        new DateInterval(
                                java.sql.Timestamp.valueOf(
                                    LocalDateTime.of(2023, 11, 1, 1, 1, 1, 1)),
                                java.sql.Timestamp.valueOf(
                                        LocalDateTime.of(2026, 12, 1, 1, 1, 1, 1))
                        ),
                        List.of(boat.getBoatId()));

        var trainingDTO = gatewayActivitiesClient.createTraining("Bearer " + userToken, trainingModel);
        var query = gatewayActivitiesClient.getTraining("Bearer " + userToken, trainingDTO.getId());

        var boat2 = addBoatToTheDatabase(
                BoatDTO.builder()
                        .name("boat 3")
                        .availablePositions(List.of(BoatRole.Coach))
                        .coxCertificateId(certificateDTO.getId()).build());

        var competitionModel =
                new CreateCompetitionModel("idk",
                        new DateInterval(java.sql.Timestamp.valueOf(
                        LocalDateTime.of(2023, 1, 1, 1, 1, 1, 1)),
                        java.sql.Timestamp.valueOf(
                                LocalDateTime.of(2026, 12, 1, 1, 1, 1, 1))),
                        List.of(boat2.getBoatId()), false, null, null);

        gatewayActivitiesClient.createCompetition("Bearer " + userToken, competitionModel);

        gatewayMatchmakingClient.autoFindActivity(
                "Bearer " + otherUserToken,
                MatchmakingStrategy.EarliestFirst,
                new ActivityFilterDTO(
                        Date.from(LocalDateTime.of(2019, 11, 1, 1, 1, 1, 1).toInstant(ZoneOffset.UTC)),
                        Date.from(LocalDateTime.of(2030, 12, 1, 1, 1, 1, 1).toInstant(ZoneOffset.UTC)),
                        List.of(BoatRole.Coach)
                )
        );

        var applications =
                gatewayMatchmakingClient.getWaitingApplications("Bearer " + otherUserToken);

        assertThatList(applications).contains(
                new UserActivityApplication(
                        query,
                        query.getBoats().get(0),
                        BoatRole.Coach
                )
        );

        gatewayMatchmakingClient.deRegisterFromActivity("Bearer " + otherUserToken, query.getId());
    }

    @AfterAll
    static void shutdownEverything() {
        microservices.forEach(x -> {
            x.stop();
            x.close();
        });
    }
}
