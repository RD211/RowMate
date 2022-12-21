package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.activities.BoatsClient;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.CertificatesClient;
import nl.tudelft.sem.project.users.UserEmail;
import nl.tudelft.sem.project.users.UsersClient;
import nl.tudelft.sem.project.users.models.ChangeCertificateNameModel;
import nl.tudelft.sem.project.users.models.ChangeCertificateSupersededModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdminControllerTest {

    @Mock
    private transient UsersClient usersClient;

    @Mock
    private transient BoatsClient boatsClient;

    @Mock
    private transient CertificatesClient certificatesClient;

    @InjectMocks
    AdminController adminController;

    @Test
    void deleteUserByUsername() {
        adminController.deleteUserByUsername("tester");
        verify(usersClient, times(1)).deleteUserByUsername(new Username("tester"));
        verifyNoMoreInteractions(usersClient);
    }

    @Test
    void deleteUserByEmail() {
        adminController.deleteUserByEmail("tester@test.com");
        verify(usersClient, times(1)).deleteUserByEmail(new UserEmail("tester@test.com"));
        verifyNoMoreInteractions(usersClient);
    }

    @Test
    void changeCertificateName() {
        var model = new ChangeCertificateNameModel(new CertificateDTO(), "Name");
        adminController.changeCertificateName(model);
        verify(certificatesClient, atLeastOnce()).changeCertificateName(model);
    }

    @Test
    void changeCertificateSuperseded() {
        var model = new ChangeCertificateSupersededModel(new CertificateDTO(), UUID.randomUUID());
        adminController.changeCertificateSuperseded(model);
        verify(certificatesClient, atLeastOnce()).changeCertificateSuperseded(model);
    }

    @Test
    void addCertificate() {
        var cert = CertificateDTO.builder().name("Some name").build();
        adminController.addCertificate(cert);
        verify(certificatesClient, atLeastOnce()).addCertificate(cert);
    }
}