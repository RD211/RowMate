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
public class MatchMakingFunctionalTests extends FunctionalTestsBase {

}
