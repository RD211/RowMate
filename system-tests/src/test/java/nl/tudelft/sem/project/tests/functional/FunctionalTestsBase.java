package nl.tudelft.sem.project.tests.functional;

import nl.tudelft.sem.project.gateway.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.subethamail.wiser.Wiser;

import java.util.List;

public class FunctionalTestsBase {

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
    GatewayCertificatesClient gatewayCertificatesClient;

    @Autowired
    GatewayMatchmakingClient gatewayMatchmakingClient;

    protected Wiser wiser;

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

    @BeforeEach
    private void initWiser() {
        wiser = new Wiser();
        wiser.setPort(587);
        wiser.setHostname("localhost");
        wiser.start();
    }

    @AfterAll
    static void shutdownEverything() {
        microservices.forEach(x -> {
            x.stop();
            x.close();
        });
    }

    @AfterEach
    private void shutdownWiser() {
        wiser.stop();
    }
}
