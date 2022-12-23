package nl.tudelft.sem.project.tests.functional;

import nl.tudelft.sem.project.gateway.*;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes=nl.tudelft.sem.project.system.tests.Application.class)
public class CertificatesFunctionalTests extends FunctionalTestsBase {
    String adminToken;
    String userToken;

    @BeforeEach
    void setup() {
        adminToken = gatewayAuthenticationClient.authenticate(
                AuthenticateUserModel.builder()
                        .username("administrator")
                        .password("administrator")
                        .build()
        );
        try {
            userToken = gatewayAuthenticationClient.register(
                    CreateUserModel.builder()
                            .username("mynameisjeff")
                            .email("good@email.com")
                            .password("ilikepancakes").build()
            );
        } catch (Exception e) {
            userToken = gatewayAuthenticationClient.authenticate(
                    AuthenticateUserModel.builder()
                            .username("mynameisjeff")
                            .password("ilikepancakes")
                            .build()
            );
        }
    }
    @Test
    void newCertificatesCanBeAddedToTheSystem() {
        var cert1 = CertificateDTO.builder().name("certificate1").build();
        var cert2 = CertificateDTO.builder().name("certificate2").build();

        var cert1Added = gatewayAdminClient.addCertificate("Bearer " + adminToken, cert1);
        var cert2Added = gatewayAdminClient.addCertificate("Bearer " + adminToken, cert2);

        assertThat(cert1Added.getName()).isEqualTo(cert1.getName());
        assertThat(cert2Added.getName()).isEqualTo(cert2.getName());

        assertThat(cert1Added.getId()).isNotNull().isNotEqualTo(cert2Added.getId());

        var cert1Fetched = gatewayCertificatesClient.getCertificateById("Bearer " + userToken, cert1Added.getId());
        var cert2Fetched = gatewayCertificatesClient.getCertificateById("Bearer " + userToken, cert2Added.getId());

        assertThat(cert1Added).isEqualTo(cert1Fetched);
        assertThat(cert2Added).isEqualTo(cert2Fetched);
    }

    @Test
    void certificateHaveASupersedenceHierarchy() {
        var cert = CertificateDTO.builder().name("A name").build();
        cert = gatewayAdminClient.addCertificate("Bearer " + adminToken, cert);

        var withSuperseded = CertificateDTO.builder().name("Another name").supersededId(cert.getId()).build();
        var withSuperseded2 = CertificateDTO.builder().name("Yet another name").supersededId(cert.getId()).build();

        withSuperseded = gatewayAdminClient.addCertificate("Bearer " + adminToken, withSuperseded);
        withSuperseded2 = gatewayAdminClient.addCertificate("Bearer " + adminToken, withSuperseded2);

        assertThat(withSuperseded.getSupersededId()).isEqualTo(cert.getId());
        assertThat(withSuperseded2.getSupersededId()).isEqualTo(cert.getId());

        var certChain = gatewayCertificatesClient.getCertificateChain("Bearer " + userToken, withSuperseded.getId());
        var certChainShort = gatewayCertificatesClient.getCertificateChain("Bearer " + userToken, cert.getId());

        assertThat(certChain).containsExactly(withSuperseded, cert);
        assertThat(certChainShort).containsExactly(cert);
    }

}
