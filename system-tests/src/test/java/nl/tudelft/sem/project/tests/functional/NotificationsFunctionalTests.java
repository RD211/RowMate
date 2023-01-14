package nl.tudelft.sem.project.tests.functional;

import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.authentication.AppUserModel;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.authentication.ResetPasswordModel;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.gateway.*;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.CertificateDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes=nl.tudelft.sem.project.system.tests.Application.class)
@ActiveProfiles("dev")
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application-dev.properties")
@WebAppConfiguration
public class NotificationsFunctionalTests extends FunctionalTestsBase{

    String adminToken;
    //private Wiser wiser;

    //@Autowired
    //private WebApplicationContext wac;
    //private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        adminToken = gatewayAuthenticationClient.authenticate(
                AuthenticateUserModel.builder()
                        .username("administrator")
                        .password("administrator")
                        .build()
        );
        //wiser = new Wiser();
        //wiser.setPort(587);
        //wiser.setHostname("localhost");
        //wiser.start();
        //mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @AfterEach
    void shutdownSmtpServer() {
        //wiser.stop();
    }

    public CertificateDTO addCertificateToTheDatabase(CertificateDTO dto) {
        var certificateDTO = gatewayAdminClient.addCertificate("Bearer " + adminToken, dto);
        assertEquals(certificateDTO.getName(), dto.getName());
        assertEquals(certificateDTO.getSupersededId(), dto.getSupersededId());
        assertNotNull(certificateDTO.getId());
        return certificateDTO;
    }

    public BoatDTO addBoatToTheDatabase(BoatDTO dto) {
        var boatDTO = gatewayAdminClient.addBoat("Bearer " + adminToken, dto);
        assertEquals(boatDTO.getName(), dto.getName());
        assertEquals(boatDTO.getAvailablePositions(), dto.getAvailablePositions());
        assertEquals(boatDTO.getCoxCertificateId(), dto.getCoxCertificateId());
        assertNotNull(boatDTO.getBoatId());
        return boatDTO;
    }

    @Test
    void registerNotificationTest() throws MessagingException, IOException {
        var createUserModel = CreateUserModel.builder()
                .username("jon_sina")
                .email("sunt_it-ist_am_bmw_si_ma_duc_in_fiecare_zi_la_loft@numaifiti.saraki")
                .password("treyh4bd5tyr").build();
        var userToken = gatewayAuthenticationClient.register(
                createUserModel
        );

        WiserMessage message = wiser.getMessages().get(wiser.getMessages().size() - 1);

        assertEquals(message.getEnvelopeReceiver(), "sunt_it-ist_am_bmw_si_ma_duc_in_fiecare_zi_la_loft@numaifiti.saraki");
        assertEquals(message.getEnvelopeSender(), "localhost");
        assertEquals(message.getMimeMessage().getSubject(), "Account successfully created!");
        assertFalse(message.getMimeMessage().getContent().toString().contains("Activity Details:"));
    }

    @Test
    void changePasswordWithPreviousNotificationTest() throws MessagingException, IOException {
        var username = "SMECHERUL";
        var password = "DIN_PASCANI";
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

        WiserMessage message = wiser.getMessages().get(wiser.getMessages().size() - 1);

        assertEquals(message.getEnvelopeReceiver(), "daviddavid@daviddavid.daviddavid");
        assertEquals(message.getEnvelopeSender(), "localhost");
        assertEquals(message.getMimeMessage().getSubject(), "Password reset");
        assertFalse(message.getMimeMessage().getContent().toString().contains("Activity Details:"));
    }

    @Test
    void joinTrainingNotificationTest() throws MessagingException, IOException {
        var newCertificate = new CertificateDTO(
                null,
                "best 34567i8o",
                null
        );

        newCertificate = addCertificateToTheDatabase(newCertificate);

        var boat = addBoatToTheDatabase(
                BoatDTO.builder().name("bestest boat").availablePositions(
                                List.of(BoatRole.Coach)
                        ).coxCertificateId(newCertificate.getId())
                        .build()
        );
        var createUserModel = CreateUserModel.builder()
                .username("marcelino")
                .email("creatorul@activitatii.ro")
                .password("treyh4bd5tyr").build();
        var joinUserModel = CreateUserModel.builder()
                .username("piscotel")
                .email("eu@sunt.sef")
                .password("simt ca o iau razna cu testele astea").build();
        var userToken = gatewayAuthenticationClient.register(
                createUserModel
        );
        var userJoinToken = gatewayAuthenticationClient.register(joinUserModel);

        var trainingModel =
                new CreateTrainingModel("idk",
                        new DateInterval(java.sql.Timestamp.valueOf(
                                LocalDateTime.of(6666, 12, 1, 1, 1, 1, 1)),
                                java.sql.Timestamp.valueOf(
                                        LocalDateTime.of(6666, 12, 1, 1, 1, 1, 1))),
                        List.of(boat.getBoatId()));

        var trainingDTO = gatewayActivitiesClient.createTraining("Bearer " + userToken, trainingModel);
        var queriedDTO =
                gatewayActivitiesClient.getTraining("Bearer " + userToken, trainingDTO.getId());

        gatewayActivityRegistrationClient.registerInActivity(
                "Bearer " + userJoinToken,
                new SeatedUserModel(
                        queriedDTO.getId(),
                        0,
                        BoatRole.Coach
                ));

        WiserMessage message = wiser.getMessages().get(wiser.getMessages().size() - 1);

        assertEquals(message.getEnvelopeReceiver(), "creatorul@activitatii.ro");
        assertEquals(message.getEnvelopeSender(), "localhost");
        assertEquals(message.getMimeMessage().getSubject(), "User joined your activity");
        assertTrue(message.getMimeMessage().getContent().toString().contains("Activity Details:"));
        assertTrue(message.getMimeMessage().getContent().toString().contains("6666"));
        assertFalse(message.getMimeMessage().getContent().toString().contains("For:"));

        message = wiser.getMessages().get(wiser.getMessages().size() - 2);

        assertEquals(message.getEnvelopeReceiver(), "eu@sunt.sef");
        assertEquals(message.getEnvelopeSender(), "localhost");
        assertEquals(message.getMimeMessage().getSubject(), "Activity joined");
        assertTrue(message.getMimeMessage().getContent().toString().contains("Activity Details:"));
        assertTrue(message.getMimeMessage().getContent().toString().contains("Your request to join an activity has been accepted!"));
        assertTrue(message.getMimeMessage().getContent().toString().contains("6666"));
        assertFalse(message.getMimeMessage().getContent().toString().contains("For:"));
    }

    @Test
    void leaveTrainingNotificationTest() throws MessagingException, IOException {
        var newCertificate = new CertificateDTO(
                null,
                "12r3122fe2fe",
                null
        );

        newCertificate = addCertificateToTheDatabase(newCertificate);

        var boat = addBoatToTheDatabase(
                BoatDTO.builder().name("bestest boat").availablePositions(
                                List.of(BoatRole.Coach)
                        ).coxCertificateId(newCertificate.getId())
                        .build()
        );
        var createUserModel = CreateUserModel.builder()
                .username("bebito")
                .email("creatorul@activitatii.edu")
                .password("treyh4bd5tyr").build();
        var joinUserModel = CreateUserModel.builder()
                .username("asddasadsadsads")
                .email("eu@sunt.popa")
                .password("simt ca o iau razna cu testele astea").build();
        var userToken = gatewayAuthenticationClient.register(
                createUserModel
        );
        var userJoinToken = gatewayAuthenticationClient.register(joinUserModel);

        var trainingModel =
                new CreateTrainingModel("idk",
                        new DateInterval(java.sql.Timestamp.valueOf(
                                LocalDateTime.of(6666, 12, 1, 1, 1, 1, 1)),
                                java.sql.Timestamp.valueOf(
                                        LocalDateTime.of(6666, 12, 1, 1, 1, 1, 1))),
                        List.of(boat.getBoatId()));

        var trainingDTO = gatewayActivitiesClient.createTraining("Bearer " + userToken, trainingModel);
        var queriedDTO =
                gatewayActivitiesClient.getTraining("Bearer " + userToken, trainingDTO.getId());

        gatewayActivityRegistrationClient.registerInActivity(
                "Bearer " + userJoinToken,
                new SeatedUserModel(
                        queriedDTO.getId(),
                        0,
                        BoatRole.Coach
                ));
        gatewayActivityRegistrationClient.deRegisterFromActivity("Bearer " + userJoinToken, trainingDTO.getId());

        WiserMessage message = wiser.getMessages().get(wiser.getMessages().size() - 1);

        assertEquals(message.getEnvelopeReceiver(), "creatorul@activitatii.edu");
        assertEquals(message.getEnvelopeSender(), "localhost");
        assertEquals(message.getMimeMessage().getSubject(), "User joined your activity");
        assertTrue(message.getMimeMessage().getContent().toString().contains("Activity Details:"));
        assertFalse(message.getMimeMessage().getContent().toString().contains("One user has left your activity."));
        assertFalse(message.getMimeMessage().getContent().toString().contains("For:"));
    }

    @Test
    void createTrainingNotificationTest() throws MessagingException, IOException {
        var newCertificate = new CertificateDTO(
                null,
                "best ce35rt",
                null
        );

        newCertificate = addCertificateToTheDatabase(newCertificate);

        var boat = addBoatToTheDatabase(
                BoatDTO.builder().name("bestest boat").availablePositions(
                                List.of(BoatRole.Coach)
                        ).coxCertificateId(newCertificate.getId())
                        .build()
        );
        var createUserModel = CreateUserModel.builder()
                .username("marcel")
                .email("adasd13a44s@gdgafm54co.3om")
                .password("treyh4bd5tyr").build();
        var userToken = gatewayAuthenticationClient.register(
                createUserModel
        );

        var trainingModel =
                new CreateTrainingModel("idk",
                        new DateInterval(java.sql.Timestamp.valueOf(
                                LocalDateTime.of(6666, 12, 1, 1, 1, 1, 1)),
                                java.sql.Timestamp.valueOf(
                                        LocalDateTime.of(6666, 12, 1, 1, 1, 1, 1))),
                        List.of(boat.getBoatId()));

        var trainingDTO = gatewayActivitiesClient.createTraining("Bearer " + userToken, trainingModel);
        var queriedDTO =
                gatewayActivitiesClient.getTraining("Bearer " + userToken, trainingDTO.getId());

        WiserMessage message = wiser.getMessages().get(wiser.getMessages().size() - 1);

        assertEquals(message.getEnvelopeReceiver(), "adasd13a44s@gdgafm54co.3om");
        assertEquals(message.getEnvelopeSender(), "localhost");
        assertEquals(message.getMimeMessage().getSubject(), "Account successfully created!");
        assertFalse(message.getMimeMessage().getContent().toString().contains("Activity Details:"));
        assertFalse(message.getMimeMessage().getContent().toString().contains("6666"));
        assertFalse(message.getMimeMessage().getContent().toString().contains("For:"));
    }

    @Test
    void createCompetitionNotificationTest() throws MessagingException, IOException {
        var newCertificate = new CertificateDTO(
                null,
                "best cert33",
                null
        );

        newCertificate = addCertificateToTheDatabase(newCertificate);

        var boat = addBoatToTheDatabase(
                BoatDTO.builder().name("bestest boat2").availablePositions(
                                List.of(BoatRole.Coach)
                        ).coxCertificateId(newCertificate.getId())
                        .build()
        );
        var createUserModel = CreateUserModel.builder()
                .username("asda3sdasda4sdas55d")
                .email("adasd133a44s@gdgafm54co.3om")
                .password("treyh34bd5tyr").build();
        var userToken = gatewayAuthenticationClient.register(
                createUserModel
        );

        var competitionModel =
                new CreateCompetitionModel("idk",
                        new DateInterval(java.sql.Timestamp.valueOf(
                                LocalDateTime.of(2026, 11, 1, 1, 1, 1, 1)),
                                java.sql.Timestamp.valueOf(
                                        LocalDateTime.of(2026, 12, 1, 1, 1, 1, 1))),
                        List.of(boat.getBoatId()),
                        true,
                        null,
                        null);

        var competitionDTO = gatewayActivitiesClient.createCompetition("Bearer " + userToken, competitionModel);
        var queriedDTO =
                gatewayActivitiesClient.getCompetition("Bearer " + userToken, competitionDTO.getId());

        WiserMessage message = wiser.getMessages().get(wiser.getMessages().size() - 1);

        assertEquals(message.getEnvelopeReceiver(), "adasd133a44s@gdgafm54co.3om");
        assertEquals(message.getEnvelopeSender(), "localhost");
        assertEquals(message.getMimeMessage().getSubject(), "Account successfully created!");
        assertFalse(message.getMimeMessage().getContent().toString().contains("Activity Details:"));
        assertFalse(message.getMimeMessage().getContent().toString().contains("2026"));
        assertFalse(message.getMimeMessage().getContent().toString().contains("For: null"));
    }
}
