package nl.tudelft.sem.project.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@EnableFeignClients("nl.tudelft.sem.project")
public class Application {
    /**
     * The starting point of the application.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
