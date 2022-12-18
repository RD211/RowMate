package nl.tudelft.sem.project.tests;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.tudelft.sem.project.authentication.AppUserModel;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.authentication.ResetPasswordModel;
import nl.tudelft.sem.project.authentication.Token;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.gateway.*;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.shared.Organization;
import nl.tudelft.sem.project.shared.Username;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;


import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes=nl.tudelft.sem.project.system.tests.Application.class)
public class LargeTests {


    @Autowired
    GatewayAuthenticationClient gatewayAuthenticationClient;

    @Autowired
    GatewayUserClient gatewayUserClient;

    @Autowired
    GatewayAdminClient gatewayAdminClient;

    static List<ConfigurableApplicationContext> microservices;
    @BeforeAll
    static void startEverything() {
        microservices = List.of(
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.users.Application.class).run("--server.port=8084"),
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.gateway.Application.class).properties("jwt.secret=exampleSecret").run("--server.port=8087"),
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.authentication.Application.class).properties("jwt.secret=exampleSecret").run("--server.port=8081"),
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.activities.Application.class).run("--server.port=8085"),
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.notifications.Application.class).run("--server.port=8086"),
                new SpringApplicationBuilder(
                        nl.tudelft.sem.project.matchmaking.Application.class).run("--server.port=8083")
        );
    }

    @AfterAll
    static void shutdownEverything() {
        microservices.forEach(Lifecycle::stop);
    }

    @ParameterizedTest
    @CsvSource({"test,test@test.com,passss3123", "tEst,tester@gmail.com,passss", "amazing_name_here,wow@wow.com,asdhjkdas"})
    void registerTest(String username, String email, String password) {
        var tokenRegister = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );
        assertNotNull(tokenRegister);
    }

    @ParameterizedTest
    @CsvSource({"test1,test2@test.com,passss3123", "tEst3,tester4@gmail.com,passss", "amazing_nam5e_here,wo6w@wow.com,asdhjkdas"})
    void authenticateTest(String username, String email, String password) {
        gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );
        var tokenAuthenticate = gatewayAuthenticationClient.authenticate(
                AuthenticateUserModel.builder()
                        .username(username)
                        .password(password).build()
        );
        assertNotNull(tokenAuthenticate);
    }

    @ParameterizedTest
    @CsvSource({"test41,test26@tes4t.com,passss3123", "tEst43,teste2r4@gma5il.com,passss", "amazin4g_nam5e_here,wo6w@5wow.com,asdhjkdas"})
    void getDetailsTest(String username, String email, String password) {
        var token = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );
        var details = gatewayUserClient.getDetailsOfUser(
                "Bearer " + token
        );
        assertNotNull(details);
        assertEquals(username, details.getUsername());
        assertEquals(email, details.getEmail());
    }

    @ParameterizedTest
    @CsvSource({"te4st41,test26@6tes4t.com,passss3123", "tEst343,teste25r4@gma5il.com,passss", "amazin4g_6nam5e_here,wo6w@5wo2w.com,asdhjkdas"})
    void changeGenderTest(String username, String email, String password) {
        var token = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );
        var details = gatewayUserClient.changeGender(
                "Bearer " + token,
                Gender.Female
        );
        assertNotNull(details);
        assertEquals(username, details.getUsername());
        assertEquals(email, details.getEmail());
        assertEquals(Gender.Female, details.getGender());
    }

    @ParameterizedTest
    @CsvSource({"te4st541,test26@6t1es4t.com,passss3123", "tEs3t343,teste255r4@gma5il.com,passss", "a3mazin4g_6nam5e_here,wo6w@5wo52w.com,asdhjkdas"})
    void changeOrganizationTest(String username, String email, String password) {
        var token = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );
        var details = gatewayUserClient.changeOrganization(
                "Bearer " + token,
                new Organization("google")
        );
        assertNotNull(details);
        assertEquals(username, details.getUsername());
        assertEquals(email, details.getEmail());
        assertEquals("google", details.getOrganization().getName());
    }

    @ParameterizedTest
    @CsvSource({"te45st541,test26@36t1es4t.com,passss3123", "tE4s3t343,teste255r14@gma5il.com,passss", "a3maz3in4g_6nam5e_here,wo6w@55wo52w.com,asdhjkdas"})
    void changeAmateurTest(String username, String email, String password) {
        var token = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );
        var details = gatewayUserClient.changeAmateur(
                "Bearer " + token,
                false
        );
        assertNotNull(details);
        assertEquals(username, details.getUsername());
        assertEquals(email, details.getEmail());
        assertFalse(details.isAmateur());
    }

    @ParameterizedTest
    @CsvSource({"te45st5451,test26@536t1es4t.com,passss3123", "tE4s33t343,teste2552r14@gma5il.com,passss", "a3maz3in4g_6n3am5e_here,wo6w@555wo52w.com,asdhjkdas"})
    void addAvailabilityTest(String username, String email, String password) {
        var token = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );
        var interval = new DateInterval(
                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)),
                Date.from(Instant.now().plus(50, ChronoUnit.DAYS))
        );
        var details = gatewayUserClient.addAvailability(
                "Bearer " + token,
                interval
        );
        assertNotNull(details);
        assertEquals(username, details.getUsername());
        assertEquals(email, details.getEmail());
        assertTrue(details.getAvailableTime().contains(interval));
    }

    @ParameterizedTest
    @CsvSource({"te455st5451,test26@5346t1es4t.com,passss3123", "tE4s331t343,teste25522r14@gma5il.com,passss", "a3ma3z3in4g_6n3am5e_here,wo6w3@555wo52w.com,asdhjkdas"})
    void removeAvailabilityTest(String username, String email, String password) {
        var token = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );
        var interval = new DateInterval(
                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)),
                Date.from(Instant.now().plus(50, ChronoUnit.DAYS))
        );
        gatewayUserClient.addAvailability(
                "Bearer " + token,
                interval
        );

        var details = gatewayUserClient.removeAvailability(
                "Bearer " + token,
                interval
        );
        assertNotNull(details);
        assertEquals(username, details.getUsername());
        assertEquals(email, details.getEmail());
        assertFalse(details.getAvailableTime().contains(interval));
    }

    @ParameterizedTest
    @CsvSource({"te45st54151,test26@5363t1es4t.com,passss3123", "tE4s333t343,teste42552r14@gma5il.com,passss", "a3maz3in41g_6n3am5e_here,wo6w@4555wo52w.com,asdhjkdas"})
    void addRoleTest(String username, String email, String password) {
        var token = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );

        var details = gatewayUserClient.addRole(
                "Bearer " + token,
                BoatRole.Cox
        );
        assertNotNull(details);
        assertEquals(username, details.getUsername());
        assertEquals(email, details.getEmail());
        assertTrue(details.getBoatRoles().contains(BoatRole.Cox));
    }

    @ParameterizedTest
    @CsvSource({"te4553st5451,test4426@5346t1e3s4t.com,passss3123", "tE4545s331t343,teste25522r14@gma35il.com,passss", "a3ma3z3in4g_6n43am5e_here,wo63w3@555wo52w.com,asdhjkdas"})
    void removeRoleTest(String username, String email, String password) {
        var token = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );

        gatewayUserClient.addRole(
                "Bearer " + token,
                BoatRole.Coach
        );

        var details = gatewayUserClient.removeRole(
                "Bearer " + token,
                BoatRole.Coach
        );
        assertNotNull(details);
        assertEquals(username, details.getUsername());
        assertEquals(email, details.getEmail());
        assertFalse(details.getBoatRoles().contains(BoatRole.Coach));
    }

    @ParameterizedTest
    @CsvSource({"te455432st5451,test44265@5346t1e3s4t.c4om,pass4ss3123", "tE4545s3341t2343,teste25522r134@gma35il4.com,p1assss", "a3ma43z3in42g_6n43am5e_here,w3o63w3@555wo52w.com,a4sdhjkdas"})
    void deleteUserByUsername(String username, String email, String password) {
        var userToken = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );

        var adminToken = gatewayAuthenticationClient.authenticate(
                AuthenticateUserModel.builder().username(
                        "administrator"
                ).password("administrator").build()
        );

        gatewayAdminClient.deleteUserByUsername(
                "Bearer " + adminToken,
                username
        );

        assertThrows(Exception.class, () -> gatewayUserClient.getDetailsOfUser(
                "Bearer " + userToken));
    }

    @ParameterizedTest
    @CsvSource({"te455432st54fg51,test44265@5346t1e3s4t.c4gfom,pass4ss3123", "tE4545s3341gft2343,teste25522r134@ggfma35il4.com,p1assss", "a3ma43z3ing42g_6n43am5e_here,w3ofg63w3@555wo52w.com,a4sdhjkdas"})
    void deleteUserByEmail(String username, String email, String password) {
        var userToken = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );

        var adminToken = gatewayAuthenticationClient.authenticate(
                AuthenticateUserModel.builder().username(
                        "administrator"
                ).password("administrator").build()
        );

        gatewayAdminClient.deleteUserByEmail(
                "Bearer " + adminToken,
                email
        );

        assertThrows(Exception.class, () -> gatewayUserClient.getDetailsOfUser(
                "Bearer " + userToken));
    }

    @ParameterizedTest
    @CsvSource({"te455432st54351,tes3t44265@5346t1e3s4t.c4om,pass4ss3123", "tE4545s33431t2343,teste25522r134@gma335il4.com,p1assss", "a3ma433z3in42g_6n43am5e_here,w3o63w3@5535wo52w.com,a4sdhjkdas"})
    void deleteUserByUsernameNotAdmin(String username, String email, String password) {
        var userToken = gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );


        assertThrows(Exception.class, () -> gatewayAdminClient.deleteUserByUsername(
                "Bearer " + userToken,
                username
        ));


        var user = gatewayUserClient.getDetailsOfUser(
                "Bearer " + userToken);
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());

    }

    @Test
    void changePasswordWithPreviousTest() {
        var username = "daviddavid";
        var password = "daviddavid";
        var newPassword = "daviddavid2";
        var email = "daviddavid@daviddavid.daviddavid";
        gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );

        gatewayAuthenticationClient.resetPasswordWithPrevious(
                new ResetPasswordModel(
                        new AppUserModel(
                                new Username(username),
                                new Password(password)
                        ),
                        new Password(newPassword)
                )
        );
        var newToken = gatewayAuthenticationClient.authenticate(
                AuthenticateUserModel.builder()
                        .username(username)
                        .password(newPassword).build()
        );
        assertNotNull(newToken);
    }

    @Test
    void getResetPasswordTokenTest() {
        var username = "daviddavid3";
        var password = "daviddavid3";
        var email = "daviddavid@daviddavid3.daviddavid";
        gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );

        assertDoesNotThrow(()->gatewayAuthenticationClient.resetPasswordWithEmail(
                new Username("daviddavid3")
        ));
    }

    @Test
    void resetPasswordWithTokenTest() {
        var username = "daviddavid4";
        var password = "daviddavid4";
        var newPassword = "daviddavid5";
        var email = "daviddavid4@daviddavid4.daviddavid4";
        gatewayAuthenticationClient.register(
                CreateUserModel.builder()
                        .username(username)
                        .email(email)
                        .password(password).build()
        );

        assertDoesNotThrow(()->gatewayAuthenticationClient.resetPasswordWithEmail(
                new Username("daviddavid4")
        ));
        var token = Jwts.builder().setSubject(username)
                .signWith(SignatureAlgorithm.HS512, "exampleSecret").compact();
        gatewayAuthenticationClient.resetPassword(token, newPassword);

        var newToken = gatewayAuthenticationClient.authenticate(
                AuthenticateUserModel.builder()
                        .username(username)
                        .password(newPassword).build()
        );
        assertNotNull(newToken);
    }
}
