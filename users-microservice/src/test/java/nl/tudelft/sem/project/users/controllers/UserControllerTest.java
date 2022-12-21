package nl.tudelft.sem.project.users.controllers;

import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.shared.Organization;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.CertificateName;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UserEmail;
import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import nl.tudelft.sem.project.users.database.repositories.UserRepository;
import nl.tudelft.sem.project.users.domain.certificate.Certificate;
import nl.tudelft.sem.project.users.domain.certificate.CertificateConverterService;
import nl.tudelft.sem.project.users.domain.certificate.CertificateService;
import nl.tudelft.sem.project.users.domain.users.*;
import nl.tudelft.sem.project.users.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {

    @Mock
    transient UserRepository userRepository;

    @Mock
    transient UserService userService;

    @Mock
    transient UserConverterService userConverterService;

    @Mock
    transient CertificateConverterService certificateConverterService;

    @Mock
    transient CertificateRepository certificateRepository;

    @Mock
    transient CertificateService certificateService;

    @InjectMocks
    UserController userController;

    @Test
    void addUserTest() {
        var userDTO = UserDTO.builder().email("user@user.com").username("user").build();
        var user = User.builder().username(new Username("user")).email(new UserEmail("user@user.com")).build();
        var savedUser =
                User.builder().username(new Username("user")).email(new UserEmail("user@user.com")).build();
        var savedUserDTO = UserDTO.builder().email("user@user.com").username("user").build();
        when(userConverterService.toEntity(userDTO)).thenReturn(user);
        when(userService.addUser(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.addUser(userDTO).getBody());

        verify(userConverterService, times(1)).toEntity(userDTO);
        verify(userService, times(1)).addUser(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService);

    }

    @Test
    void getUserByUsernameTest() {
        var username = new Username("user");
        var userDTO = UserDTO.builder().email("user@user.com").username("user").build();
        var user =
                User.builder().username(username).email(new UserEmail("user@user.com")).build();

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(userConverterService.toDTO(user)).thenReturn(userDTO);

        assertEquals(userDTO, userController.getUserByUsername(username).getBody());

        verify(userService, times(1)).getUserByUsername(username);
        verify(userConverterService, times(1)).toDTO(user);
        verifyNoMoreInteractions(userConverterService, userService);
    }

    @Test
    void changeGenderTest() {
        var userDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .gender(Gender.NonBinary)
                .build();

        var user =
                User.builder()
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .gender(Gender.NonBinary)
                        .build();

        var savedUser = User.builder()
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .gender(Gender.Female)
                .build();

        var savedUserDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .gender(Gender.Female)
                .build();

        final var changeGenderModel = new ChangeGenderUserModel(
                userDTO,
                Gender.Female
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.changeGender(changeGenderModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository);
    }

    @Test
    void changeAmateurTest() {
        var userDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .isAmateur(true)
                .build();

        var user =
                User.builder()
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .isAmateur(true)
                        .build();

        var savedUser = User.builder()
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .isAmateur(false)
                .build();

        var savedUserDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .isAmateur(false)
                .build();

        final var changeAmateurUserModel = new ChangeAmateurUserModel(
                userDTO,
                false
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.changeAmateur(changeAmateurUserModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository);
    }

    @Test
    void changeOrganizationTest() {
        var userDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .organization(new Organization("google"))
                .build();

        var user =
                User.builder()
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .organization(new Organization("google"))
                        .build();

        var savedUser = User.builder()
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .organization(new Organization("facebook"))
                .build();

        var savedUserDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .organization(new Organization("facebook"))
                .build();

        final var changeOrganizationUserModel = new ChangeOrganizationUserModel(
                userDTO,
                new Organization("facebook")
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.changeOrganization(changeOrganizationUserModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository);
    }

    @Test
    void addAvailabilityTest() {
        Set<DateInterval> initialTimes = new HashSet<>(
                List.of(new DateInterval(
                        Date.from(Instant.now()),
                        Date.from(Instant.now().plus(10, ChronoUnit.DAYS))))
        );

        DateInterval newDateInterval = new DateInterval(
                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)),
                Date.from(Instant.now().plus(50, ChronoUnit.DAYS))
        );

        Set<DateInterval> newTimes = new HashSet<>(
                List.of(
                        new DateInterval(
                                Date.from(Instant.now()),
                                Date.from(Instant.now().plus(10, ChronoUnit.DAYS))),
                        newDateInterval
                )
        );

        var userDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .availableTime(initialTimes)
                .build();

        var user =
                User.builder()
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .availableTime(initialTimes)
                        .build();

        var savedUser = User.builder()
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .availableTime(newTimes)
                .build();

        var savedUserDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .availableTime(newTimes)
                .build();

        final var addAvailabilityModel = new AddAvailabilityUserModel(
                userDTO,
                newDateInterval
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.addAvailability(addAvailabilityModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository);
    }

    @Test
    void removeAvailabilityTest() {
        Set<DateInterval> initialTimes = new HashSet<>(
                List.of(new DateInterval(
                        Date.from(Instant.now()),
                        Date.from(Instant.now().plus(10, ChronoUnit.DAYS))),
                        new DateInterval(
                                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)),
                                Date.from(Instant.now().plus(50, ChronoUnit.DAYS))
                        ))
        );

        DateInterval removeDateInterval = new DateInterval(
                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)),
                Date.from(Instant.now().plus(50, ChronoUnit.DAYS))
        );

        Set<DateInterval> newTimes = new HashSet<>(
                List.of(
                        new DateInterval(
                                Date.from(Instant.now()),
                                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                )
        );

        var uuid = UUID.randomUUID();
        var userDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .availableTime(initialTimes)
                .build();

        var user =
                User.builder()
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .availableTime(initialTimes)
                        .build();

        var savedUser = User.builder()
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .availableTime(newTimes)
                .build();

        var savedUserDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .availableTime(newTimes)
                .build();

        final var removeAvailabilityModel = new RemoveAvailabilityUserModel(
                userDTO,
                removeDateInterval
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.removeAvailability(removeAvailabilityModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository);
    }

    @Test
    void addRoleTest() {
        Set<BoatRole> initialRoles = new HashSet<>(
                List.of(BoatRole.Coach)
        );

        BoatRole newRole = BoatRole.Cox;

        Set<BoatRole> newRoles = new HashSet<>(
                List.of(BoatRole.Coach, newRole)
        );

        var userDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .boatRoles(initialRoles)
                .build();

        var user =
                User.builder()
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .boatRoles(initialRoles)
                        .build();

        var savedUser = User.builder()
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .boatRoles(newRoles)
                .build();

        var savedUserDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .boatRoles(newRoles)
                .build();

        final var addRoleUserModel = new AddRoleUserModel(
                userDTO,
                newRole
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.addRole(addRoleUserModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository);
    }

    @Test
    void removeAvailability() {
        Set<BoatRole> initialRoles = new HashSet<>(
                List.of(BoatRole.Coach, BoatRole.Cox)
        );

        BoatRole removeRole = BoatRole.Coach;

        Set<BoatRole> newRoles = new HashSet<>(
                List.of(BoatRole.Cox)
        );

        var userDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .boatRoles(initialRoles)
                .build();

        var user =
                User.builder()
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .boatRoles(initialRoles)
                        .build();

        var savedUser = User.builder()
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .boatRoles(newRoles)
                .build();

        var savedUserDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .boatRoles(newRoles)
                .build();

        final var removeRoleUserModel = new RemoveRoleUserModel(
                userDTO,
                removeRole
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.removeRole(removeRoleUserModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository);
    }

    @Test
    void addCertificateTest() {
        var firstCert = new Certificate("cert B");
        var certToAdd = new Certificate("Cert D");

        Set<Certificate> initialCerts = new HashSet<>(
                List.of(firstCert)
        );

        Set<Certificate> newCerts = new HashSet<>(
                List.of(firstCert, certToAdd)
        );

        when(certificateConverterService.toDTO(firstCert))
                .thenReturn(CertificateDTO.builder()
                        .id(firstCert.getId())
                        .name(firstCert.getName().getValue())
                        .build());
        when(certificateConverterService.toDTO(certToAdd))
                .thenReturn(CertificateDTO.builder()
                        .id(certToAdd.getId())
                        .name(certToAdd.getName().getValue())
                        .build());

        var uuid = UUID.randomUUID();
        var userDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .certificates(initialCerts.stream()
                        .map(c -> certificateConverterService.toDTO(c)).collect(Collectors.toSet()))
                .build();

        var user =
                User.builder()
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .certificates(initialCerts)
                        .build();

        var savedUser = User.builder()
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .certificates(newCerts)
                .build();

        var savedUserDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .certificates(newCerts.stream()
                        .map(c -> certificateConverterService.toDTO(c)).collect(Collectors.toSet()))
                .build();

        final var addCertificateUserModel = new AddCertificateUserModel(
                userDTO,
                certificateConverterService.toDTO(certToAdd)
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);
        when(certificateRepository.findById(certToAdd.getId())).thenReturn(Optional.of(certToAdd));

        assertEquals(savedUserDTO, userController.addCertificate(addCertificateUserModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verify(certificateRepository, times(1)).findById(certToAdd.getId());
        verifyNoMoreInteractions(userConverterService, userService, userRepository, certificateRepository);
    }

    @Test
    void removeCertificateTest() {
        var certToRemove = new Certificate("cert A");
        var otherCert = new Certificate("cert B");

        Set<Certificate> initialCerts = new HashSet<>(
                List.of(certToRemove, otherCert)
        );

        Set<Certificate> newCerts = new HashSet<>(
                List.of(otherCert)
        );

        when(certificateConverterService.toDTO(certToRemove))
                .thenReturn(CertificateDTO.builder()
                        .id(certToRemove.getId())
                        .name(certToRemove.getName().getValue())
                        .build());
        when(certificateConverterService.toDTO(otherCert))
                .thenReturn(CertificateDTO.builder()
                        .id(otherCert.getId())
                        .name(otherCert.getName().getValue())
                        .build());

        var uuid = UUID.randomUUID();
        var userDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .certificates(initialCerts.stream()
                        .map(c -> certificateConverterService.toDTO(c)).collect(Collectors.toSet()))
                .build();

        var user =
                User.builder()
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .certificates(initialCerts)
                        .build();

        var savedUser = User.builder()
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .certificates(newCerts)
                .build();

        var savedUserDTO = UserDTO.builder()
                .email("user@user.com")
                .username("user")
                .certificates(newCerts.stream()
                        .map(c -> certificateConverterService.toDTO(c)).collect(Collectors.toSet()))
                .build();

        final var removeCertificateUserModel = new RemoveCertificateUserModel(
                userDTO,
                certificateConverterService.toDTO(certToRemove)
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);
        when(certificateRepository.findById(certToRemove.getId())).thenReturn(Optional.of(certToRemove));

        assertEquals(savedUserDTO, userController.removeCertificate(removeCertificateUserModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verify(certificateRepository, times(1)).findById(certToRemove.getId());
        verifyNoMoreInteractions(userConverterService, userService, userRepository, certificateRepository);
    }

    @Test
    void deleteUserByUsernameTest() {
        userController.deleteUserByUsername(new Username("user"));

        verify(userService, times(1)).deleteUserByUsername(new Username("user"));
        verifyNoMoreInteractions(userConverterService, userService);
    }

    @Test
    void deleteUserByEmailTest() {
        userController.deleteUserByEmail(new UserEmail("user@usering.user"));

        verify(userService, times(1)).deleteUserByEmail(new UserEmail("user@usering.user"));
        verifyNoMoreInteractions(userConverterService, userService);
    }

    @Test
    void hasCertificate() {
        var cert1 = new Certificate("root cert");

        var cert2 = new Certificate("node cert", cert1);

        var cert3 = new Certificate("leaf cert", cert2);

        var cert4 = new Certificate("leaf cert2", cert1);

        var user = User.builder().username(
                new Username("userr")
        ).certificates(Set.of(
                cert3
        )).build();


        when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        when(certificateService.getCertificateById(cert1.getId())).thenReturn(cert1);
        when(certificateService.getCertificateById(cert2.getId())).thenReturn(cert2);
        when(certificateService.getCertificateById(cert3.getId())).thenReturn(cert3);
        when(certificateService.getCertificateById(cert4.getId())).thenReturn(cert4);

        assertEquals(Boolean.TRUE, userController.hasCertificate(user.getUsername(), cert1.getId()).getBody());
        assertEquals(Boolean.TRUE, userController.hasCertificate(user.getUsername(), cert2.getId()).getBody());
        assertEquals(Boolean.FALSE, userController.hasCertificate(user.getUsername(), cert4.getId()).getBody());

    }
}