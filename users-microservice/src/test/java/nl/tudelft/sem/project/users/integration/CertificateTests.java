package nl.tudelft.sem.project.users.integration;

import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import nl.tudelft.sem.project.users.domain.certificate.Certificate;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.SessionFactory;


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
        Configuration configuration = new Configuration().configure();
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();

        Certificate cert1 = new Certificate("Certificate 1");
        Certificate cert2 = new Certificate("Certificate 2", cert1);
        session.save(cert1);
        session.save(cert2);
        session.getTransaction().commit();
        session.close();

//        session.
//
//        System.out.println(cert1.getId() + " " + savedCert1.getId());
//
//        var certFromRepo = certificateRepository.findById(savedCert2.getId());
//        System.out.println(certFromRepo);
////
//        assertThat(certFromRepo.isPresent());
//        assertThat(certFromRepo.get().hasInChain(savedCert1));
    }

}
