package nl.tudelft.sem.project.matchmaking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Matchmaking microservice application.
 */
@SpringBootApplication
@EnableFeignClients("nl.tudelft.sem.project")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
