package nl.tudelft.sem.project.users.domain.certificate;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import nl.tudelft.sem.project.users.CertificateName;
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


import java.util.Collection;
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

    Certificate existingCertificate;
    Certificate existingWithSupersededCertificate;
    Certificate withNonExistentSupersededCertificate;
    Certificate nonExistentCertificate;

    @BeforeEach
    void setup() {
        existingCertificate = new Certificate("Valid name");
        existingWithSupersededCertificate = new Certificate("Another name", existingCertificate);
        nonExistentCertificate = new Certificate("Non-existent name");
        withNonExistentSupersededCertificate = new Certificate("Another name", nonExistentCertificate);


        when(certificateRepository.findById(existingCertificate.getId()))
                .thenReturn(Optional.of(existingCertificate));
        when(certificateRepository.findById(existingWithSupersededCertificate.getId()))
                .thenReturn(Optional.of(existingWithSupersededCertificate));
        when(certificateRepository.findById(nonExistentCertificate.getId()))
                .thenReturn(Optional.empty());

        when(certificateRepository.existsById(existingCertificate.getId()))
                .thenReturn(true);
        when(certificateRepository.existsById(existingWithSupersededCertificate.getId()))
                .thenReturn(true);
        when(certificateRepository.existsById(nonExistentCertificate.getId()))
                .thenReturn(false);

        when(certificateRepository.findByName(existingCertificate.getName()))
                .thenReturn(Optional.of(existingCertificate));
        when(certificateRepository.findByName(existingWithSupersededCertificate.getName()))
                .thenReturn(Optional.of(existingWithSupersededCertificate));
        when(certificateRepository.findByName(nonExistentCertificate.getName()))
                .thenReturn(Optional.empty());

        when(certificateRepository.existsByName(existingCertificate.getName()))
                .thenReturn(true);
        when(certificateRepository.existsByName(existingWithSupersededCertificate.getName()))
                .thenReturn(true);
        when(certificateRepository.existsByName(nonExistentCertificate.getName()))
                .thenReturn(false);
        when(certificateRepository.existsByName(withNonExistentSupersededCertificate.getName()))
                .thenReturn(false);

        when(certificateRepository.findAll())
                .thenReturn(new ArrayIterator<>(new Certificate[] {existingCertificate, existingWithSupersededCertificate}));
    }

    @Test
    void addNullCertificateThrows() {
        assertThatThrownBy(() -> certificateService.addCertificate(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void addCertificateWithNoSuperseding() {
        certificateService.addCertificate(nonExistentCertificate);

        verify(certificateRepository, atLeastOnce()).save(nonExistentCertificate);
    }

    @Test
    void addCertificateWithNameCollision() {
        assertThatThrownBy(() -> certificateService.addCertificate(existingCertificate))
                .isInstanceOf(CertificateNameInUseException.class);

        verify(certificateRepository, never()).save(existingCertificate);

    }

    @Test
    void addCertificateWithMissingSuperseding() {
        assertThatThrownBy(() -> certificateService.addCertificate(withNonExistentSupersededCertificate))
                .isInstanceOf(CertificateNotFoundException.class);

        verify(certificateRepository, never()).save(withNonExistentSupersededCertificate);
    }

    @Test
    void addCertificateWithWorkingSuperseding() {
        withNonExistentSupersededCertificate.setSuperseded(existingCertificate);

        certificateService.addCertificate(withNonExistentSupersededCertificate);

        verify(certificateRepository, atLeastOnce()).save(withNonExistentSupersededCertificate);
    }

    @Test
    void getCertificateByNullIdThrows() {
        assertThatThrownBy(() -> certificateService.getCertificateById(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getCertificateByIdMissing() {
        assertThatThrownBy(() -> certificateService.getCertificateById(nonExistentCertificate.getId()))
                .isInstanceOf(CertificateNotFoundException.class);
    }

    @Test
    void getCertificateById() {
        assertThat(certificateService.getCertificateById(existingCertificate.getId()))
                .isEqualTo(existingCertificate);
    }

    @Test
    void updateCertificateWithNullsThrows() {
        assertThatThrownBy(
                () -> certificateService.updateCertificateName(null, new CertificateName("A name"))
        ).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(
                () -> certificateService.updateCertificateName(new Certificate(), null)
        ).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(
                () -> certificateService.updateCertificateSuperseded(null, Optional.empty())
        ).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(
                () -> certificateService.updateCertificateSuperseded(new Certificate(), null)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void updateCertificateNonexistent() {
        assertThatThrownBy(
                () -> certificateService.updateCertificateName(nonExistentCertificate, new CertificateName("A name"))
        ).isInstanceOf(CertificateNotFoundException.class);

        assertThatThrownBy(
                () -> certificateService.updateCertificateSuperseded(nonExistentCertificate, Optional.empty())
        ).isInstanceOf(CertificateNotFoundException.class);
    }

    @Test
    void updateCertificateNameThatAlreadyExists() {
        var certificate = new Certificate("ABC");
        assertThatThrownBy(() -> certificateService.updateCertificateName(certificate, existingCertificate.getName()))
                .isInstanceOf(CertificateNameInUseException.class);
    }

    @Test
    void updateCertificateNameWorking() {
        var newName = new CertificateName("Updated name");
        when(certificateRepository.save(existingCertificate)).thenReturn(existingCertificate);
        when(certificateRepository.existsByName(newName)).thenReturn(false);

        var updated = certificateService.updateCertificateName(existingCertificate, newName);

        assertThat(updated.getName()).isEqualTo(newName);

        verify(certificateRepository, atLeastOnce()).save(existingCertificate);
    }

    @Test
    void updateCertificateSupersedingWithEmpty() {
        when(certificateRepository.save(existingWithSupersededCertificate)).thenReturn(existingWithSupersededCertificate);

        var updated = certificateService.updateCertificateSuperseded(
                existingWithSupersededCertificate, Optional.empty());

        assertThat(updated.getSuperseded()).isEmpty();
        verify(certificateRepository, atLeastOnce()).save(existingWithSupersededCertificate);
    }

    @Test
    void updateCertificateSupersedingWithExistentSuperseded() {
        when(certificateRepository.save(existingCertificate)).thenReturn(existingCertificate);

        var updated = certificateService.updateCertificateSuperseded(
                existingCertificate, Optional.of(existingWithSupersededCertificate.getId()));

        assertThat(updated.getSuperseded()).hasValue(existingWithSupersededCertificate);
        verify(certificateRepository, atLeastOnce()).save(existingCertificate);
    }


    @Test
    void updateCertificateWithNonExistentSuperseding() {
        assertThatThrownBy(
                () -> certificateService.updateCertificateSuperseded(
                        existingCertificate, Optional.of(nonExistentCertificate.getId()))
        ).isInstanceOf(CertificateNotFoundException.class);
    }

    @Test
    void existsByIdWithNullThrows() {
        assertThatThrownBy(() -> certificateService.existsById(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void existsByNameWithNullThrows() {
        assertThatThrownBy(() -> certificateService.existsByName(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getCertificateByNameWithNullThrows() {
        assertThatThrownBy(() -> certificateService.getCertificateByName(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getCertificateByNameThatDoesNotExist() {
        assertThatThrownBy(
                () -> certificateService.getCertificateByName(new CertificateName("Non-existent name")))
                .isInstanceOf(CertificateNotFoundException.class);
    }

    @Test
    void getCertificateByNameWorking() {
        var cert = certificateService.getCertificateByName(existingCertificate.getName());
        assertThat(cert).isEqualTo(existingCertificate);
    }

    @Test
    void getAllCertificates() {
        Collection<Certificate> certs = certificateService.getAllCertificates();
        assertThat(certs).containsExactly(existingCertificate, existingWithSupersededCertificate);
    }
}