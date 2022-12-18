package nl.tudelft.sem.project.users.domain.certificate;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CertificateTest {

    @Test
    void certificateChainWithOne() {
        var cert1 = new Certificate("certificate1");

        assertThat(cert1.getAllFromCertificateChain()).containsExactly(cert1);
        assertThat(cert1.hasInChain(cert1)).isTrue();
    }

    @Test
    void certificateChainWithMany() {
        var cert3 = new Certificate("certificate3");
        var cert1 = new Certificate("certificate1", cert3);
        var cert2 = new Certificate("certificate2", cert1);

        assertThat(cert1.getAllFromCertificateChain()).containsExactly(cert1, cert3);
        assertThat(cert2.getAllFromCertificateChain()).containsExactly(cert2, cert1, cert3);

        assertThat(cert1.hasInChain(cert3)).isTrue();
        assertThat(cert2.hasInChain(cert3)).isTrue();
        assertThat(cert3.hasInChain(cert1)).isFalse();
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void certificateChainWithCircularSupersedence() {
        var cert1 = new Certificate("certificate1");
        var cert2 = new Certificate("certificate2", cert1);

        cert1.setSuperseded(cert2);

        assertThat(cert1.getAllFromCertificateChain()).containsExactly(cert1, cert2);
        assertThat(cert1.hasInChain(cert2)).isTrue();
        assertThat(cert1.hasInChain(new Certificate("Some other"))).isFalse();
    }

    @Test
    void certificateIsEqualToItself() {
        var cert1 = new Certificate("certificate1");
        assertThat(cert1).isEqualTo(cert1);
    }

    @Test
    void certificatesWithTheSameIdAreAlwaysEqual() {
        var cert1 = new Certificate("certificate");
        var cert2 = new Certificate("other certificate");

        cert2.setId(cert1.getId());

        assertThat(cert1).isEqualTo(cert2);
    }

    @Test
    void certificatesWithDifferentIdsAreNotEqual() {
        var cert1 = new Certificate("certificate");
        var cert2 = new Certificate("certificate");

        assertThat(cert1).isNotEqualTo(cert2);
    }

    @Test
    void hashCodeUsesOnlyIds() {
        var cert1 = new Certificate("certificate");
        var cert2 = new Certificate("other certificate");

        cert2.setId(cert1.getId());

        assertThat(cert1).hasSameHashCodeAs(cert2);
    }

    @Test
    void constructorWithNullSupersedingShouldThrow() {
        assertThatThrownBy(() -> new Certificate("A name", null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void getSuperseding() {
        var cert1 = new Certificate("Cert1");
        var cert2 = new Certificate("Cert2");

        assertThat(cert2.getSuperseded()).isEmpty();

        cert2.setSuperseded(cert1);

        assertThat(cert2.getSuperseded()).hasValue(cert1);
    }
}