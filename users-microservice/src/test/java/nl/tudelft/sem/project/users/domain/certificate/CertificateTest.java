package nl.tudelft.sem.project.users.domain.certificate;

import nl.tudelft.sem.project.entities.users.CertificateDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CertificateTest {

    @Test
    void testCertificateChainOne() {
        var cert1 = new Certificate("certificate1", null);

        assertEquals(cert1.getAllFromCertificateChain(), List.of(cert1));
    }

    @Test
    void testCertificateChainMany() {
        var cert3 = new Certificate("certificate2", null);
        var cert1 = new Certificate("certificate1", cert3, null);
        var cert2 = new Certificate("certificate2", cert1, null);

        assertEquals(cert1.getAllFromCertificateChain(), List.of(cert1, cert3));
        assertEquals(cert2.getAllFromCertificateChain(), List.of(cert2, cert1, cert3));
    }

    @Test
    void testCertificateChainCircular() {
        var cert1 = new Certificate("certificate1", null);
        var cert2 = new Certificate("certificate2", cert1, null);

        cert1.setSuperseded(cert2);

        assertEquals(cert1.getAllFromCertificateChain(), List.of(cert1, cert2));
    }

    @Test
    void testEqualsSameObject() {
        var cert1 = new Certificate("certificate1", null);
        assertEquals(cert1, cert1);
    }

    @Test
    void testEqualsWithNull() {
        var cert1 = new Certificate("certificate1", null);
        assertNotEquals(cert1, null);
    }

    @Test
    void testEqualsWithOtherClass() {
        var cert1 = new Certificate("certificate1", null);
        assertNotEquals(cert1, new String("abc"));
    }

    @Test
    void testEqualsUsesOnlyId() {
        var cert1 = new Certificate("certificate", null);
        var cert2 = new Certificate("other certificate", null);

        cert2.setId(cert1.getId());

        assertEquals(cert1, cert2);
    }

    @Test
    void testEqualsDifferent() {
        var cert1 = new Certificate("certificate", null);
        var cert2 = new Certificate("other certificate", null);

        assertNotEquals(cert1, cert2);
    }

    @Test
    void testHashCodeUsesOnlyId() {
        var cert1 = new Certificate("certificate", null);
        var cert2 = new Certificate("other certificate", null);

        cert2.setId(cert1.getId());

        assertEquals(cert1.hashCode(), cert2.hashCode());
    }

    @Test
    void testSetSupersededDoesNotAllowNulls() {
        var cert = new Certificate("certificate", null);

        assertThrows(NullPointerException.class, () -> cert.setSuperseded(null));
    }

    @Test
    void testNoNullCertificateAllowed() {
        assertThrows(NullPointerException.class, () -> new Certificate("cert", null, null));
    }

    @Test
    void testToDTOWithSuperseding() {
        var other = new Certificate("other", null);
        var cert = new Certificate("cert", other, null);

        var dto = cert.toDTO();
        assertTrue(dto.getSupersededId().isPresent());
        assertEquals(dto.getSupersededId().get(), other.getId());
    }

    @Test
    void testToDTOWithoutSuperseding() {
        var cert = new Certificate("cert", null);

        var dto = cert.toDTO();
        assertTrue(dto.getSupersededId().isEmpty());
    }

    @Test
    void testFromDTO() {
        var dto = new CertificateDTO(UUID.randomUUID(), "cert_name", Optional.empty(), null);
        var certFromDTO = new Certificate(dto);
        var cert = new Certificate("cert_name", null);
        assertEquals(certFromDTO.getName(), cert.getName());
        assertEquals(certFromDTO.getSuperseded(), cert.getSuperseded());
        assertEquals(certFromDTO.getForBoat(), cert.getForBoat());
    }
}