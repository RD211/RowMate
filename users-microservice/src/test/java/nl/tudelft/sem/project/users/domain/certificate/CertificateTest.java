package nl.tudelft.sem.project.users.domain.certificate;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CertificateTest {

    @Test
    void testCertificateChainOne() {
        var cert1 = new Certificate("certificate1");

        assertEquals(cert1.getAllFromCertificateChain(), List.of(cert1));
        assertTrue(cert1.hasInChain(cert1));
    }

    @Test
    void testCertificateChainMany() {
        var cert3 = new Certificate("certificate3");
        var cert1 = new Certificate("certificate1", cert3);
        var cert2 = new Certificate("certificate2", cert1);

        assertEquals(cert1.getAllFromCertificateChain(), List.of(cert1, cert3));
        assertEquals(cert2.getAllFromCertificateChain(), List.of(cert2, cert1, cert3));

        assertTrue(cert1.hasInChain(cert3));
        assertTrue(cert2.hasInChain(cert3));
        assertFalse(cert3.hasInChain(cert1));
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testCertificateChainCircular() {
        var cert1 = new Certificate("certificate1");
        var cert2 = new Certificate("certificate2", cert1);

        cert1.setSuperseded(cert2);

        assertEquals(cert1.getAllFromCertificateChain(), List.of(cert1, cert2));
        assertTrue(cert1.hasInChain(cert2));
        assertFalse(cert1.hasInChain(new Certificate("Some other")));
    }

    @Test
    void testEqualsSameObject() {
        var cert1 = new Certificate("certificate1");
        assertEquals(cert1, cert1);
    }

    @Test
    void testEqualsUsesOnlyId() {
        var cert1 = new Certificate("certificate");
        var cert2 = new Certificate("other certificate");

        cert2.setId(cert1.getId());

        assertEquals(cert1, cert2);
    }

    @Test
    void testEqualsDifferent() {
        var cert1 = new Certificate("certificate");
        var cert2 = new Certificate("other certificate");

        assertNotEquals(cert1, cert2);
    }

    @Test
    void testHashCodeUsesOnlyId() {
        var cert1 = new Certificate("certificate");
        var cert2 = new Certificate("other certificate");

        cert2.setId(cert1.getId());

        assertEquals(cert1.hashCode(), cert2.hashCode());
    }


//    @Test
//    void testToDTOWithSuperseding() {
//        var other = new Certificate("other");
//        var cert = new Certificate("cert", other);
//
//        var dto = cert.toDTO();
//        assertTrue(dto.getSupersededId().isPresent());
//        assertEquals(dto.getSupersededId().get(), other.getId());
//    }
//
//    @Test
//    void testToDTOWithoutSuperseding() {
//        var cert = new Certificate("cert");
//
//        var dto = cert.toDTO();
//        assertTrue(dto.getSupersededId().isEmpty());
//    }
//
//    @Test
//    void testFromDTOWithoutSuperseding() throws CertificateNotFoundException {
//        var dto = new CertificateDTO(UUID.randomUUID(), "cert_name", Optional.empty());
//        var certFromDTO = new Certificate(dto, null);
//        var cert = new Certificate("cert_name");
//
//        assertEquals(certFromDTO.getName(), cert.getName());
//        assertEquals(certFromDTO.getSuperseded(), cert.getSuperseded());
//    }
//
//    @Test
//    void testFromDTOWithSupersedingPresent() throws CertificateNotFoundException {
//        CertificateRepository repo = Mockito.mock(CertificateRepository.class);
//        var supersededCertificate = new Certificate("superseded_cert");
//        Mockito.when(repo.findById(supersededCertificate.getId())).thenReturn(Optional.of(supersededCertificate));
//
//        var dto = new CertificateDTO(UUID.randomUUID(), "cert_name", Optional.of(supersededCertificate.getId()));
//        var certFromDTO = new Certificate(dto, repo);
//
//        assertTrue(certFromDTO.getSuperseded().isPresent());
//        assertEquals(certFromDTO.getSuperseded().get(), supersededCertificate);
//    }
//
//    @Test
//    void testFromDTOWithSupersedingMissing() {
//        CertificateRepository repo = Mockito.mock(CertificateRepository.class);
//        UUID nonexistentCertId = UUID.randomUUID();
//        Mockito.when(repo.findById(nonexistentCertId)).thenReturn(Optional.empty());
//
//        var dto = new CertificateDTO(UUID.randomUUID(), "cert_name", Optional.of(nonexistentCertId));
//
//        assertThrows(CertificateNotFoundException.class, () -> new Certificate(dto, repo));
//
//    }
}