package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.BoatsClient;
import nl.tudelft.sem.project.enums.BoatRole;
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
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

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
        var response = adminController.deleteUserByUsername("tester");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(usersClient, times(1)).deleteUserByUsername(new Username("tester"));
        verifyNoMoreInteractions(usersClient);
    }

    @Test
    void deleteUserByEmail() {
        var response = adminController.deleteUserByEmail("tester@test.com");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(usersClient, times(1)).deleteUserByEmail(new UserEmail("tester@test.com"));
        verifyNoMoreInteractions(usersClient);
    }

    @Test
    void changeCertificateName() {
        var certificate = CertificateDTO.builder()
                .name("C4")
                .build();
        var model = new ChangeCertificateNameModel(certificate, "4+");
        var changedCertificate = CertificateDTO.builder().name("4+").build();
        when(certificatesClient.changeCertificateName(model)).thenReturn(changedCertificate);

        var response = adminController.changeCertificateName(model);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getName()).isEqualTo(changedCertificate.getName());
        verify(certificatesClient, atLeastOnce()).changeCertificateName(model);
    }

    @Test
    void changeCertificateSuperseded() {
        var model = new ChangeCertificateSupersededModel(new CertificateDTO(), UUID.randomUUID());

        var response = adminController.changeCertificateSuperseded(model);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(certificatesClient, atLeastOnce()).changeCertificateSuperseded(model);
    }

    @Test
    void addCertificate() {
        var cert = CertificateDTO.builder().name("Some name").build();

        var response = adminController.addCertificate(cert);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(certificatesClient, atLeastOnce()).addCertificate(cert);
    }

    @Test
    void deleteBoat() {
        var boatUUID = UUID.randomUUID();

        var response = adminController.deleteBoat(boatUUID);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(boatsClient, times(1)).deleteBoat(boatUUID);
    }

    @Test
    void removePositionFromBoat() {
        var position = BoatRole.Coach;
        var boatUUID = UUID.randomUUID();

        var response = adminController.removePositionFromBoat(boatUUID, position);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(boatsClient, times(1)).removePositionFromBoat(boatUUID, position);
    }

    @Test
    void addBoat() {
        var coxCertificateUUID = UUID.randomUUID();
        var boat = BoatDTO.builder()
                .name("Canoe")
                .availablePositions(List.of(BoatRole.Cox, BoatRole.PortSideRower, BoatRole.StarboardSideRower))
                .coxCertificateId(coxCertificateUUID)
                .build();
        when(certificatesClient.getCertificateById(coxCertificateUUID))
                .thenReturn(CertificateDTO.builder().id(coxCertificateUUID).name("C4").build());
        when(boatsClient.addBoat(boat)).thenReturn(boat);

        var response = adminController.addBoat(boat);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(boat);
        verify(certificatesClient, atLeastOnce()).getCertificateById(coxCertificateUUID);
        verify(boatsClient, times(1)).addBoat(boat);
    }

    @Test
    void addPositionToBoat() {
        var position = BoatRole.StarboardSideRower;
        var boatUUID = UUID.randomUUID();

        var response = adminController.addPositionToBoat(boatUUID, position);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(boatsClient, times(1)).addPositionToBoat(boatUUID, position);
    }

    @Test
    void changeCoxCertifcate() {
        var coxCertificateUUID = UUID.randomUUID();
        var boatUUID = UUID.randomUUID();
        when(certificatesClient.getCertificateById(coxCertificateUUID))
                .thenReturn(CertificateDTO.builder().id(coxCertificateUUID).name("C4").build());

        var response = adminController.changeCoxCertificate(boatUUID, coxCertificateUUID);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(certificatesClient, atLeastOnce()).getCertificateById(coxCertificateUUID);
        verify(boatsClient, times(1)).changeCoxCertificate(boatUUID, coxCertificateUUID);
    }

    @Test
    void renameBoat() {
        var boatUUID = UUID.randomUUID();
        var newBoatName = "Kayak";

        var response = adminController.renameBoat(boatUUID, newBoatName);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(boatsClient, atLeastOnce()).renameBoat(boatUUID, newBoatName);
    }
}