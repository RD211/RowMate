package nl.tudelft.sem.project.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Example microservice application.
 */
@SpringBootApplication
@EnableFeignClients("nl.tudelft.sem.project")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
