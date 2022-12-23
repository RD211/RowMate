package nl.tudelft.sem.project.notifications;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.subethamail.wiser.Wiser;

public class TestSMTP {

    private Wiser wiser;

    @BeforeEach
    private void initSmtpServer() {
        wiser = new Wiser();
        wiser.setPort(587);
        wiser.setHostname("localhost");
        wiser.start();
    }

    @AfterEach
    private void closeSmtpServer() {
        wiser.stop();
    }
}
