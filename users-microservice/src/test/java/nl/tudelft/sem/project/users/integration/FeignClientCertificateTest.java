package nl.tudelft.sem.project.users.integration;

import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.CertificatesClient;
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

    @ParameterizedTest
    @CsvSource({"A normal certificate name", "894142134 jrhk21r213hrlc 41234", "ABC"})
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
        String certificateName = strOfLen(len);

        assertThatThrownBy(() -> certificatesClient.addCertificate(
                CertificateDTO.builder().name(certificateName).build()
        ));
    }

    @ParameterizedTest
    @CsvSource({"2", "50"})
    void certificateNameBoundaryTestsOk(int len) {
        String certificateName = strOfLen(len);

        var response = certificatesClient.addCertificate(
                CertificateDTO.builder().name(certificateName).build()
        );

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(certificateName);
    }

    String strOfLen(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append('A');
        }
        return sb.toString();
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
        ));
    }
}
