package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.gateway.authentication.AuthManager;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.shared.Organization;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.CertificatesClient;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import nl.tudelft.sem.project.users.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {

    @Mock
    private transient AuthManager authManager;

    @Mock
    private transient UsersClient usersClient;

    @Mock
    private transient CertificatesClient certificatesClient;

    @InjectMocks
    UserController userController;

    UserDTO userDTO;

    @BeforeEach
    void setUp() {
        when(authManager.getUsername()).thenReturn("tester_master");
        userDTO = UserDTO.builder().email("tester@test.com").username("tester_master").build();
        when(usersClient.getUserByUsername(new Username("tester_master"))).thenReturn(userDTO);
    }

    @Test
    void getDetailsOfUser() {
        var details = userController.getDetailsOfUser().getBody();
        assertEquals(userDTO, details);
    }

    @Test
    void changeGender() {
        when(usersClient.changeGenderOfUser(
                new ChangeGenderUserModel(
                        UserDTO.builder().username("tester_master").build(),
                        Gender.Female
                )
        )).thenReturn(
                userDTO.withGender(Gender.Female)
        );
        var details = userController.changeGender(Gender.Female).getBody();
        assertEquals(userDTO.withGender(Gender.Female), details);
    }

    @Test
    void changeOrganization() {
        when(usersClient.changeOrganizationOfUser(
                new ChangeOrganizationUserModel(
                        UserDTO.builder().username("tester_master").build(),
                        new Organization("google")
                )
        )).thenReturn(
                userDTO.withOrganization(new Organization("google"))
        );
        var details = userController.changeOrganization(new Organization("google")).getBody();
        assertEquals(userDTO.withOrganization(new Organization("google")), details);
    }

    @Test
    void changeIfUserIsAmateur() {
        when(usersClient.changeAmateurOfUser(
                new ChangeAmateurUserModel(
                        UserDTO.builder().username("tester_master").build(),
                        false
                )
        )).thenReturn(
                userDTO.withAmateur(false)
        );
        var details = userController.changeIfUserIsAmateur(false).getBody();
        assertEquals(userDTO.withAmateur(false), details);
    }

    @Test
    void addAvailability() {
        var newDate = new DateInterval(
                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)),
                Date.from(Instant.now().plus(50, ChronoUnit.DAYS))
        );
        when(usersClient.addAvailabilityToUser(
                new AddAvailabilityUserModel(
                        UserDTO.builder().username("tester_master").build(),
                        newDate
                )
        )).thenReturn(
                userDTO.withAvailableTime(Set.of(newDate))
        );
        var details = userController.addAvailability(newDate).getBody();
        assertEquals(userDTO.withAvailableTime(Set.of(newDate)), details);
    }

    @Test
    void removeAvailability() {
        var newDate = new DateInterval(
                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)),
                Date.from(Instant.now().plus(50, ChronoUnit.DAYS))
        );
        when(usersClient.getUserByUsername(new Username("tester_master"))).thenReturn(userDTO.withAvailableTime(
                Set.of(newDate)
        ));

        when(usersClient.removeAvailabilityOfUser(
                new RemoveAvailabilityUserModel(
                        UserDTO.builder().username("tester_master").build(),
                        newDate
                )
        )).thenReturn(
                userDTO.withAvailableTime(Set.of())
        );
        var details = userController.removeAvailability(newDate).getBody();
        assertEquals(userDTO.withAvailableTime(Set.of()), details);
    }

    @Test
    void addRole() {
        when(usersClient.addRoleToUser(
                new AddRoleUserModel(
                        UserDTO.builder().username("tester_master").build(),
                        BoatRole.Cox
                )
        )).thenReturn(
                userDTO.withBoatRoles(Set.of(BoatRole.Cox))
        );
        var details = userController.addRole(BoatRole.Cox).getBody();
        assertEquals(userDTO.withBoatRoles(Set.of(BoatRole.Cox)), details);
    }

    @Test
    void removeRole() {

        when(usersClient.getUserByUsername(new Username("tester_master"))).thenReturn(userDTO.withBoatRoles(
                Set.of(BoatRole.Cox)
        ));

        when(usersClient.removeRoleFromUser(
                new RemoveRoleUserModel(
                        UserDTO.builder().username("tester_master").build(),
                        BoatRole.Cox
                )
        )).thenReturn(
                userDTO.withBoatRoles(Set.of())
        );
        var details = userController.removeRole(BoatRole.Cox).getBody();
        assertEquals(userDTO.withBoatRoles(Set.of()), details);
    }

    @Test
    void addCertificate() {
        var cert = CertificateDTO.builder().name("cert 1").build();
        when(usersClient.addCertificateToUser(
                new AddCertificateUserModel(
                        UserDTO.builder().username("tester_master").build(),
                        cert
                )
        )).thenReturn(
                userDTO.withCertificates(Set.of(cert))
        );

        var certId = UUID.randomUUID();
        when(certificatesClient.getCertificateById(certId)).thenReturn(cert);
        var details = userController.addCertificate(certId).getBody();
        assertEquals(userDTO.withCertificates(Set.of(cert)), details);
    }

    @Test
    void removeCertificate() {
        var cert = CertificateDTO.builder().name("cert 1").build();
        when(usersClient.getUserByUsername(new Username("tester_master"))).thenReturn(userDTO.withCertificates(
                Set.of(cert)
        ));

        when(usersClient.removeCertificateFromUser(
                new RemoveCertificateUserModel(
                        UserDTO.builder().username("tester_master").build(),
                        cert
                )
        )).thenReturn(
                userDTO.withCertificates(Set.of())
        );

        var certId = UUID.randomUUID();
        when(certificatesClient.getCertificateById(certId)).thenReturn(cert);
        var details = userController.removeCertificate(certId).getBody();
        assertEquals(userDTO.withCertificates(Set.of()), details);
    }
}