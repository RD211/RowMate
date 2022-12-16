package nl.tudelft.sem.project.users.domain.certificate;

import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;

import nl.tudelft.sem.project.users.exceptions.CertificateNameInUseException;
import nl.tudelft.sem.project.users.exceptions.CertificateNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CertificateServiceTest {

    @Mock
    CertificateRepository certificateRepository;

    @InjectMocks
    private CertificateService certificateService;

    @Test
    void addNullCertificateThrows() {
        assertThatThrownBy(() -> certificateService.addCertificate(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void addCertificateWithNoSuperseding() {
        when(certificateRepository.existsByName(any())).thenReturn(false);
        var certificate = new Certificate("Certificate name");

        certificateService.addCertificate(certificate);

        verify(certificateRepository, atLeastOnce()).save(certificate);
    }

    @Test
    void addCertificateWithNameCollision() {
        var name = new CertificateName("CertificateName");
        when(certificateRepository.existsByName(name)).thenReturn(true);

        var certificate = new Certificate(name.getValue());

        assertThatThrownBy(() -> certificateService.addCertificate(certificate))
                .isInstanceOf(CertificateNameInUseException.class);

        verify(certificateRepository, never()).save(certificate);

    }

    @Test
    void addCertificateWithMissingSuperseding() {
        when(certificateRepository.existsByName(any())).thenReturn(false);
        var otherCertificate = new Certificate("SomeName");
        var certificate = new Certificate("CertificateName", otherCertificate);

        when(certificateRepository.existsById(otherCertificate.getId())).thenReturn(false);

        assertThatThrownBy(() -> certificateService.addCertificate(certificate))
                .isInstanceOf(CertificateNotFoundException.class);

        verify(certificateRepository, never()).save(certificate);
    }

    @Test
    void addCertificateWithWorkingSuperseding() {
        when(certificateRepository.existsByName(any())).thenReturn(false);
        var otherCertificate = new Certificate("SomeName");
        var certificate = new Certificate("CertificateName", otherCertificate);

        when(certificateRepository.existsById(otherCertificate.getId())).thenReturn(true);

        certificateService.addCertificate(certificate);

        verify(certificateRepository, atLeastOnce()).save(certificate);
    }

    @Test
    void getCertificateByNullIdThrows() {

        assertThatThrownBy(() -> certificateService.getCertificateById(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getCertificateByIdMissing() {

        var certificate = new Certificate("CertificateName");
        when(certificateRepository.findById(certificate.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> certificateService.getCertificateById(certificate.getId()))
                .isInstanceOf(CertificateNotFoundException.class);
    }

    @Test
    void getCertificateById() {

        var certificate = new Certificate("CertificateName");
        when(certificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));

        assertThat(certificateService.getCertificateById(certificate.getId())).isEqualTo(certificate);
    }

    @Test
    void updateCertificateWithNullThrows() {
        assertThatThrownBy(() -> certificateService.updateCertificate(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void updateCertificateNonexistent() {
        var certificate = new Certificate("CertificateName");


        assertThatThrownBy(() -> certificateService.updateCertificate(null)).isInstanceOf(NullPointerException.class);
    }


    @Test
    void getAllCertificates() {
    }
}