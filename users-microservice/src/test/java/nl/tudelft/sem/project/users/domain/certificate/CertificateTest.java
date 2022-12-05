package nl.tudelft.sem.project.users.domain.certificate;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CertificateTest {

    @Test
    void testCertificateChainOne() {
        var cert1 = new Certificate("certificate1", null, null);
        assertEquals(cert1.getAllFromCertificateChain(), List.of(cert1));
    }

    @Test
    void testCertificateChainMany() {
        var cert3 = new Certificate("certificate2", null, null);
        var cert1 = new Certificate("certificate1", cert3, null);
        var cert2 = new Certificate("certificate2", cert1, null);

        assertEquals(cert1.getAllFromCertificateChain(), List.of(cert1, cert3));
        assertEquals(cert2.getAllFromCertificateChain(), List.of(cert2, cert1, cert3));
    }

    @Test
    void testCertificateChainCircular() {
        var cert1 = new Certificate("certificate1", null, null);
        var cert2 = new Certificate("certificate2", cert1, null);
        cert1.setSupersedes(cert2);

        assertEquals(cert1.getAllFromCertificateChain(), List.of(cert1, cert2));
    }

    @Test
    void testEqualsSameObject() {
        var cert1 = new Certificate("certificate1", null, null);
        assertEquals(cert1, cert1);
    }

    @Test
    void testEqualsWithNull() {
        var cert1 = new Certificate("certificate1", null, null);
        assertNotEquals(cert1, null);
    }

    @Test
    void testEqualsWithOtherClass() {
        var cert1 = new Certificate("certificate1", null, null);
        System.out.println(cert1.getId());
        assertNotEquals(cert1, new String("abc"));
    }

    @Test
    void testEqualsUsesOnlyId() {
        var cert1 = new Certificate("certificate", null, null);
        var cert2 = new Certificate("other certificate", null, null);

        UUID id = new UUID(123, 567);
        cert1.setId(id);
        cert2.setId(id);

        assertEquals(cert1, cert2);
    }

    @Test
    void testEqualsDifferent() {
        var cert1 = new Certificate("certificate", null, null);
        var cert2 = new Certificate("other certificate", null, null);

        UUID id1 = new UUID(123, 567);
        UUID id2 = new UUID(321, 765);
        cert1.setId(id1);
        cert2.setId(id2);

        assertNotEquals(cert1, cert2);
    }

    @Test
    void testHashCodeUsesOnlyId() {
        var cert1 = new Certificate("certificate", null, null);
        var cert2 = new Certificate("other certificate", null, null);

        UUID id = new UUID(123, 567);
        cert1.setId(id);
        cert2.setId(id);

        assertEquals(cert1.hashCode(), cert2.hashCode());
    }
}