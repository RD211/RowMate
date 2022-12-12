package nl.tudelft.sem.project.users.integration;

import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import nl.tudelft.sem.project.users.domain.certificate.Certificate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class CertificateTests {

    @Autowired
    private transient CertificateRepository certificateRepository;

    @Test
    public void repositoryStoresRecursiveCertificates() {

        Certificate cert1 = new Certificate("Certificate 1");
        cert1 = certificateRepository.save(cert1);
        Certificate cert2 = new Certificate("Certificate 2", cert1);
        cert2 = certificateRepository.save(cert2);

        Optional<Certificate> fromRepo = certificateRepository.findByName("Certificate 2");

        assertThat(fromRepo.isPresent()).isTrue();
        assertThat(fromRepo.get().getAllFromCertificateChain()).containsAll(List.of(cert1, cert2));
    }

}
