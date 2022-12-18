package nl.tudelft.sem.project.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Users microservice application.
 */
@SpringBootApplication
@EnableJpaRepositories("nl.tudelft.sem.project.users.database.repositories")
@EnableFeignClients("nl.tudelft.sem.project")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
