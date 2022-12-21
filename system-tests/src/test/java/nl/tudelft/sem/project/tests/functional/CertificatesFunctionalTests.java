package nl.tudelft.sem.project.tests.functional;

import nl.tudelft.sem.project.gateway.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;

import java.util.List;

@SpringBootTest(classes=nl.tudelft.sem.project.system.tests.Application.class)
public class CertificatesFunctionalTests {
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
}
