package nl.tudelft.sem.project.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Users microservice application.
 */
@SpringBootApplication
@EnableJpaRepositories("nl.tudelft.sem.project.users.database.repositories")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
