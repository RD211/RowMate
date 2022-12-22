package nl.tudelft.sem.project.users.integration;

import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.CertificatesClient;
import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import nl.tudelft.sem.project.users.models.ChangeCertificateNameModel;
import nl.tudelft.sem.project.users.models.ChangeCertificateSupersededModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class FeignClientCertificateTest {

    @Autowired
    CertificatesClient certificatesClient;

    @Autowired
    CertificateRepository certificateRepository;

    @BeforeEach
    void setup() {
        certificateRepository.deleteAll();
    }

    @ParameterizedTest
    @CsvSource({"A normal certificate name", "894142134 jrhk21r213hrlc 41234", "C4"})
    void addingCertificatesWithValidNamesTest(String certificateName) {
        var response = certificatesClient.addCertificate(
                CertificateDTO.builder().name(certificateName).build()
        );

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(certificateName);
        assertThat(response.getSupersededId()).isNull();
    }

    @ParameterizedTest
    @CsvSource({"1", "51"})
    void certificateNameBoundaryTestsBad(int len) {
        String certificateName = "A".repeat(len);

        assertThatThrownBy(() -> certificatesClient.addCertificate(
                CertificateDTO.builder().name(certificateName).build()
        )).hasMessageContaining("Name must be between 2 and 50 characters");
    }

    @ParameterizedTest
    @CsvSource({"2", "50"})
    void certificateNameBoundaryTestsOk(int len) {
        String certificateName = "A".repeat(len);

        var response = certificatesClient.addCertificate(
                CertificateDTO.builder().name(certificateName).build()
        );

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(certificateName);
    }

    @Test
    void addingAndGettingCertificatesWithChaining() {
        var added = certificatesClient.addCertificate(
                CertificateDTO.builder().name("A valid name").build()
        );
        var withSuperseded = certificatesClient.addCertificate(
                CertificateDTO.builder().name("Another name").supersededId(added.getId()).build()
        );

        assertThat(withSuperseded.getSupersededId()).isEqualTo(added.getId());

        var first = certificatesClient.getCertificateById(added.getId());
        var second = certificatesClient.getCertificateById(withSuperseded.getId());

        assertThat(first).isEqualTo(added);
        assertThat(second).isEqualTo(withSuperseded);
    }

    @Test
    void addCertificateWithInvalidChaining() {
        var nonExistentId = UUID.randomUUID();

        assertThatThrownBy(() -> certificatesClient.addCertificate(
                CertificateDTO.builder().name("Ok name").supersededId(nonExistentId).build()
        )).hasMessageContaining("id").hasMessageContaining(nonExistentId.toString());
    }

    @Test
    void addCertificateWithPreexistingName() {
        var existing = certificatesClient.addCertificate(
                CertificateDTO.builder().name("A name").build()
        );

        assertThatThrownBy(
                () -> certificatesClient.addCertificate(
                        CertificateDTO.builder().name(existing.getName()).build()
                )
        ).hasMessageContaining("name").hasMessageContaining(existing.getName());
    }

    @Test
    void gettingVariousCertificates() {
        var certificate = certificatesClient.addCertificate(
                CertificateDTO.builder().name("A name").build()
        );
        var withSuperseding = certificatesClient.addCertificate(
                CertificateDTO.builder().name("Another name").supersededId(certificate.getId()).build()
        );
        var anotherCertificate = certificatesClient.addCertificate(
                CertificateDTO.builder().name("Some name").build()
        );

        assertThat(certificatesClient.getAllAvailableCertificates())
                .containsExactlyInAnyOrder(certificate, withSuperseding, anotherCertificate);

        assertThat(certificatesClient.getCertificateByName("A name")).isEqualTo(certificate);
        assertThat(certificatesClient.getCertificateById(anotherCertificate.getId())).isEqualTo(anotherCertificate);

        assertThat(certificatesClient.getCertificateChain(withSuperseding.getId()))
                .containsExactly(withSuperseding, certificate);
    }

    @Test
    void gettingNonExistentCertificates() {
        var nonExistentId = UUID.randomUUID();
        assertThatThrownBy(() -> certificatesClient.getCertificateById(nonExistentId))
                .hasMessageContaining(nonExistentId.toString())
                .hasMessageContaining("id");

        assertThatThrownBy(() -> certificatesClient.getCertificateChain(nonExistentId))
                .hasMessageContaining(nonExistentId.toString())
                .hasMessageContaining("id");

        var nonExistentName = "A non-existent name";
        assertThatThrownBy(() -> certificatesClient.getCertificateByName(nonExistentName))
                .hasMessageContaining(nonExistentName)
                .hasMessageContaining("name");

    }

    @Test
    void updatingCertificates() {
        final var certificate = certificatesClient.addCertificate(CertificateDTO.builder().name("A nice name").build());
        var testCert = certificatesClient.addCertificate(CertificateDTO.builder().name("Another name").build());

        testCert = certificatesClient.changeCertificateName(
                new ChangeCertificateNameModel(testCert, "Another nice name"));
        var fetched = certificatesClient.getCertificateById(testCert.getId());
        assertThat(fetched).isEqualTo(testCert);
        assertThat(fetched.getName()).isEqualTo("Another nice name");

        testCert = certificatesClient.changeCertificateSuperseded(
                new ChangeCertificateSupersededModel(testCert, certificate.getId()));
        fetched = certificatesClient.getCertificateById(testCert.getId());
        assertThat(fetched).isEqualTo(testCert);
        assertThat(fetched.getSupersededId()).isEqualTo(certificate.getId());

        testCert = certificatesClient.changeCertificateSuperseded(
                new ChangeCertificateSupersededModel(testCert, null));
        fetched = certificatesClient.getCertificateById(testCert.getId());
        assertThat(fetched).isEqualTo(testCert);
        assertThat(fetched.getSupersededId()).isEqualTo(null);
    }

    @Test
    void updatingCertificatesBad() {
        var testCert = certificatesClient.addCertificate(CertificateDTO.builder().name("A nice name").build());
        var nonExistentCert = CertificateDTO.builder().name("A name").id(UUID.randomUUID()).build();

        assertThatThrownBy(
                () -> certificatesClient.changeCertificateName(
                        new ChangeCertificateNameModel(nonExistentCert, "A name"))
        ).hasMessageContaining(nonExistentCert.getId().toString()).hasMessageContaining("id");

        assertThatThrownBy(
                () -> certificatesClient.changeCertificateName(
                        new ChangeCertificateNameModel(testCert, "_"))
        ).hasMessageContaining("Name must be between 2 and 50 characters");

        assertThatThrownBy(
                () -> certificatesClient.changeCertificateName(
                        new ChangeCertificateNameModel(testCert, testCert.getName()))
        ).hasMessageContaining("name").hasMessageContaining(testCert.getName());

        assertThatThrownBy(
                () -> certificatesClient.changeCertificateSuperseded(
                        new ChangeCertificateSupersededModel(nonExistentCert, testCert.getId()))
        ).hasMessageContaining(nonExistentCert.getId().toString()).hasMessageContaining("id");

        assertThatThrownBy(
                () -> certificatesClient.changeCertificateSuperseded(
                        new ChangeCertificateSupersededModel(testCert, nonExistentCert.getId()))
        ).hasMessageContaining(nonExistentCert.getId().toString()).hasMessageContaining("id");
    }
}
