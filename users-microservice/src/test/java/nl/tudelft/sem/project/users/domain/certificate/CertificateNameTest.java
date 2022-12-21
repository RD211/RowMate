package nl.tudelft.sem.project.users.domain.certificate;

import nl.tudelft.sem.project.users.CertificateName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CertificateNameTest {


    @Test
    void constructCertificateName() {
        var certName = new CertificateName("Correct certificate name");
    }

    @Test
    void getValue() {
        var certName = new CertificateName("Correct certificate name");
        assertThat(certName.getValue()).isEqualTo("Correct certificate name");
    }

    @Test
    void nameTooShort() {
        assertThatThrownBy(() -> new CertificateName("A")).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void nameTooLong() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 51; i++) {
            s.append("A");
        }
        String name = s.toString();
        assertThatThrownBy(() -> new CertificateName(name)).isInstanceOf(ConstraintViolationException.class);
    }
}