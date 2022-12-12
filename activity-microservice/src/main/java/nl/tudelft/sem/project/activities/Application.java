package nl.tudelft.sem.project.activities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Activities microservice application.
 */
@SpringBootApplication
@EnableJpaRepositories("nl.tudelft.sem.project.activities.database.repository")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
