package nl.tudelft.sem.project.authentication.integration;

import nl.tudelft.sem.project.authentication.AppUserModel;
import nl.tudelft.sem.project.authentication.AuthClient;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.shared.Username;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class FeignClientTests {
    @Autowired
    AuthClient authClient;

    @ParameterizedTest
    @CsvSource({"test,good_pass", "tEst,amazing_pass", "amazing_name_here,amazing_pass"})
    void registerValidTest(String username, String password) {
        assertDoesNotThrow(() -> authClient.register(
                AppUserModel.builder()
                        .username(new Username(username))
                        .password(new Password(password)).build()
        ));
    }

    @ParameterizedTest
    @CsvSource({"AAAA,AAAAAAAAAA", "BBBB,AAAAAAAAAA", "CCCC,AAAAAAAAAA"})
    void authenticateValidTest(String username, String password) {
        var appModel = AppUserModel.builder()
                .username(new Username(username))
                .password(new Password(password)).build();
        assertDoesNotThrow(() -> authClient.register(
                AppUserModel.builder()
                        .username(new Username(username))
                        .password(new Password(password)).build()
        ));

        var response = authClient.authenticate(appModel);

        assertNotNull(response);
    }
}
