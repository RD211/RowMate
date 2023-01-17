package nl.tudelft.sem.project.users.controllers;

import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UserEmail;
import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import nl.tudelft.sem.project.users.database.repositories.UserRepository;
import nl.tudelft.sem.project.users.domain.certificate.Certificate;
import nl.tudelft.sem.project.users.domain.certificate.CertificateConverterService;
import nl.tudelft.sem.project.users.domain.certificate.CertificateService;
import nl.tudelft.sem.project.users.domain.users.User;
import nl.tudelft.sem.project.users.domain.users.UserConverterService;
import nl.tudelft.sem.project.users.domain.users.UserService;
import nl.tudelft.sem.project.users.models.AddCertificateUserModel;
import nl.tudelft.sem.project.users.models.RemoveCertificateUserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserCertificateControllerTest {

    @Mock
    transient CertificateConverterService certificateConverterService;

    @Mock
    transient CertificateRepository certificateRepository;

    @Mock
    transient CertificateService certificateService;

    @Mock
    transient UserRepository userRepository;

    @Mock
    transient UserService userService;

    @Mock
    transient UserConverterService userConverterService;


    @InjectMocks
    UserCertificateController userCertificateController;

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

        assertEquals(Boolean.TRUE, userCertificateController.hasCertificate(user.getUsername(), cert1.getId()).getBody());
        assertEquals(Boolean.TRUE, userCertificateController.hasCertificate(user.getUsername(), cert2.getId()).getBody());
        assertEquals(Boolean.FALSE, userCertificateController.hasCertificate(user.getUsername(), cert4.getId()).getBody());

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

        assertEquals(savedUserDTO, userCertificateController.removeCertificate(removeCertificateUserModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository, certificateRepository);
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

        assertEquals(savedUserDTO, userCertificateController.addCertificate(addCertificateUserModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository, certificateRepository);
    }
}
