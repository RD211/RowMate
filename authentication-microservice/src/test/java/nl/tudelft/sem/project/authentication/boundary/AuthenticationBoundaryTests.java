package nl.tudelft.sem.project.authentication.boundary;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.tudelft.sem.project.authentication.AppUserModel;
import nl.tudelft.sem.project.authentication.AuthClient;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.authentication.ResetPasswordModel;
import nl.tudelft.sem.project.shared.Username;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class AuthenticationBoundaryTests {
    @Autowired
    AuthClient authClient;

    @ParameterizedTest
    @CsvSource({"aaaa,123456", "aaaaa,1234567", "aaaaba,23423434123",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,"
                    + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void registerValidTest(String username, String password) {
        assertDoesNotThrow(() -> authClient.register(
                AppUserModel.builder()
                        .username(new Username(username))
                        .password(new Password(password)).build()
        ));
    }

    @ParameterizedTest
    @CsvSource({"aaaA,123456", "aaaaA,1234567", "aaaabA,23423434123",
            "aaaaaaaaaaAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,"
                    + "aaaaaaaAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void authenticateValidTest(String username, String password) {
        var appModel = AppUserModel.builder()
                .username(new Username(username))
                .password(new Password(password)).build();

        assertDoesNotThrow(() -> authClient.register(appModel));
        var response = authClient.authenticate(appModel);
        assertNotNull(response);
    }

    @ParameterizedTest
    @CsvSource({"a", "aa", "aaa",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void registerInvalidUsernameTest(String username) {
        assertThrows(ConstraintViolationException.class, () -> authClient.register(
                AppUserModel.builder()
                        .username(new Username(username))
                        .password(new Password("12345678")).build()
        ));
    }

    @ParameterizedTest
    @CsvSource({"a", "aa", "aaa", "aaaa", "aaaaa",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void registerInvalidPasswordTest(String password) {
        assertThrows(ConstraintViolationException.class, () -> authClient.register(
                AppUserModel.builder()
                        .username(new Username("dasdasd"))
                        .password(new Password(password)).build()
        ));
    }

    @ParameterizedTest
    @CsvSource({"a", "aa", "aaa",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void authenticateInvalidUsernameTest(String username) {
        assertThrows(ConstraintViolationException.class, () -> authClient.authenticate(
                AppUserModel.builder()
                        .username(new Username(username))
                        .password(new Password("12345678")).build()
        ));
    }

    @ParameterizedTest
    @CsvSource({"a", "aa", "aaa", "aaaa", "aaaaa",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void authenticateInvalidPasswordTest(String password) {
        assertThrows(ConstraintViolationException.class, () -> authClient.authenticate(
                AppUserModel.builder()
                        .username(new Username("dasdasd"))
                        .password(new Password(password)).build()
        ));
    }

    @ParameterizedTest
    @CsvSource({"aAaA,123456,123451", "aAaaA,1234567,645645", "aAaabA,23423434123,good_pass",
            "aaaaaaaaaaAaaaaAAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,"
                    + "aaaaaaaAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,"
                    + "aaaaaaaAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void resetPasswordValidTest(String username, String password, String newPassword) {
        var appModel = AppUserModel.builder()
                .username(new Username(username))
                .password(new Password(password)).build();
        var resetPasswordModel = new ResetPasswordModel(
                appModel,
                new Password(newPassword)
        );

        assertDoesNotThrow(() -> authClient.register(appModel));
        assertDoesNotThrow(() -> authClient.authenticate(appModel));
        assertDoesNotThrow(() -> authClient.resetPasswordWithPrevious(resetPasswordModel));

        var response = authClient.authenticate(appModel.withPassword(new Password(newPassword)));
        assertNotNull(response);
    }

    @ParameterizedTest
    @CsvSource({"aAaH,123456,a", "aAaHA,1234567,aa", "aAaHbA,23423434123,aaa",
            "aAaHfbA,23423434123,aaaaa",
            "aaaaHaaaaaAaaaaAAaaaaaaaaaaaaaaaaaaAaaaaaaaaaaaaaa,"
                    + "aaaaaaaAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,"
                    + "aaaaaaaAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void resetPasswordInvalidTest(String username, String password, String newPassword) {
        var appModel = AppUserModel.builder()
                .username(new Username(username))
                .password(new Password(password)).build();

        assertDoesNotThrow(() -> authClient.register(appModel));
        assertDoesNotThrow(() -> authClient.authenticate(appModel));
        assertThrows(ConstraintViolationException.class,
                () -> authClient.resetPasswordWithPrevious(new ResetPasswordModel(
                        appModel,
                        new Password(newPassword)
                )));

        assertThrows(Exception.class,
                () -> authClient.authenticate(appModel.withPassword(new Password(newPassword))));
    }

    @ParameterizedTest
    @CsvSource({"fAaA,123456,123451", "aAbaA,1234567,645645", "aAasbA,23423434123,good_pass",
            "aaaaaaaaaaAeaaaAAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,"
                    + "aaaaaaaAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,"
                    + "aaaaaaaAaaaaaaaaaaaaaaaaaaaaaaaaaaDaaaaaaaaaaaaaaa"})
    void emailResetPasswordValidTest(String username, String password, String newPassword) {
        var appModel = AppUserModel.builder()
                .username(new Username(username))
                .password(new Password(password)).build();
        assertDoesNotThrow(() -> authClient.register(appModel));
        assertDoesNotThrow(() -> authClient.authenticate(appModel));

        var token = Jwts.builder().setSubject(username)
                .signWith(SignatureAlgorithm.HS512, "exampleSecret").compact();
        assertDoesNotThrow(() -> authClient.emailResetPassword(token, new Password(newPassword)));

        var response = authClient.authenticate(appModel.withPassword(new Password(newPassword)));
        assertNotNull(response);

        assertThrows(Exception.class, () -> authClient.authenticate(appModel));
    }

    @ParameterizedTest
    @CsvSource({"fAaAb,123456,a", "aAbbaA,1234567,aa", "aAasbbA,23423434123,aaa",
            "aAasbbAA,23423434123,aaaa", "abAasbvAA,23423434123,aaaaa",
            "aaaaaaaaaaBeaaaAAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,"
                    + "aaaaaaaAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,"
                    + "aaaaaaaAaaaaaaaaaaaaaaaaaaaaaaaaaaDaaaaaaaaaaaaaaaa"})
    void emailResetPasswordInvalidTest(String username, String password, String newPassword) {
        var appModel = AppUserModel.builder()
                .username(new Username(username))
                .password(new Password(password)).build();
        assertDoesNotThrow(() -> authClient.register(appModel));
        assertDoesNotThrow(() -> authClient.authenticate(appModel));

        var token = Jwts.builder().setSubject(username)
                .signWith(SignatureAlgorithm.HS512, "exampleSecret").compact();
        assertThrows(Exception.class, () -> authClient.emailResetPassword(token, new Password(newPassword)));

        assertThrows(Exception.class, () -> authClient.authenticate(appModel.withPassword(new Password(newPassword))));
    }


}
