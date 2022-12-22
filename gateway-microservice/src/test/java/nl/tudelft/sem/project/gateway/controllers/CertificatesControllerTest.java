package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.CertificatesClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CertificatesControllerTest {

    @Mock
    private transient CertificatesClient certificatesClient;

    @InjectMocks
    private transient CertificatesController certificatesController;

    @Test
    void getCertificateById() {
        UUID id = UUID.randomUUID();
        var cert = CertificateDTO.builder().id(id).name("A name").build();
        when(certificatesClient.getCertificateById(id)).thenReturn(cert);
        assertThat(certificatesController.getCertificateById(id)).isEqualTo(ResponseEntity.ok(cert));
        verify(certificatesClient, atLeastOnce()).getCertificateById(id);
    }

    @Test
    void getCertificateByName() {
        String name = "A name";
        var cert = CertificateDTO.builder().name(name).build();
        when(certificatesClient.getCertificateByName(name)).thenReturn(cert);
        assertThat(certificatesController.getCertificateByName(name)).isEqualTo(ResponseEntity.ok(cert));
        verify(certificatesClient, atLeastOnce()).getCertificateByName(name);
    }

    @Test
    void getAllAvailableCertificates() {
        var certs = List.of(new CertificateDTO(null, "A1", null), new CertificateDTO(null, "A2", null));
        when(certificatesClient.getAllAvailableCertificates()).thenReturn(certs);
        assertThat(certificatesController.getAllAvailableCertificates()).isEqualTo(ResponseEntity.ok(certs));
        verify(certificatesClient, atLeastOnce()).getAllAvailableCertificates();
    }

    @Test
    void getCertificateChain() {
        var c1 = new CertificateDTO(UUID.randomUUID(), "A1", null);
        var c2 = new CertificateDTO(UUID.randomUUID(), "A1", c1.getId());
        var certs = List.of(c2, c1);
        when(certificatesClient.getCertificateChain(c2.getId())).thenReturn(certs);
        assertThat(certificatesController.getCertificateChain(c2.getId())).isEqualTo(ResponseEntity.ok(certs));
        verify(certificatesClient, atLeastOnce()).getCertificateChain(c2.getId());
    }
}