package nl.tudelft.sem.template.authentication;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import nl.tudelft.sem.project.entities.users.ExampleFeignUsers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class Application {
    /**
     * The starting point of the application.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {

        // Example of how to create a feign client.
        // Temporary feign way to communicate with the users microservice
        // ExampleFeignUsers client = Feign.builder()
        //        .encoder(new GsonEncoder())
        //        .decoder(new GsonDecoder())
        //        .target(ExampleFeignUsers.class, "http://localhost:8084");


        SpringApplication.run(Application.class, args);
    }
}
