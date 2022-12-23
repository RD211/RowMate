package nl.tudelft.sem.project.tests.functional;

import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.gateway.*;
import nl.tudelft.sem.project.users.CertificateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes=nl.tudelft.sem.project.system.tests.Application.class)
public class BoatsFunctionalTests extends FunctionalTestsBase {

    String adminToken;
    String userToken;

    @BeforeEach
    void setup() {
        adminToken = gatewayAuthenticationClient.authenticate(
                AuthenticateUserModel.builder().username(
                        "administrator"
                ).password("administrator").build()
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

    public CertificateDTO addCertificateToTheDatabase(CertificateDTO dto) {
        var certificateDTO = gatewayAdminClient.addCertificate("Bearer " + adminToken, dto);
        assertEquals(certificateDTO.getName(), dto.getName());
        assertEquals(certificateDTO.getSupersededId(), dto.getSupersededId());
        assertNotNull(certificateDTO.getId());
        return certificateDTO;
    }

    @Test
    void addBoatTest() {
        var cert = new CertificateDTO(
                null,
                "best ce745rt",
                null
        );

        cert = addCertificateToTheDatabase(cert);

        var boat = BoatDTO.builder()
                .name("good_boat")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.Cox))
                .coxCertificateId(cert.getId())
                .build();
        var boatDTO = gatewayAdminClient.addBoat("Bearer " + adminToken, boat);
        assertEquals(boatDTO.getName(), "good_boat");
        assertEquals(boatDTO.getAvailablePositions(), List.of(BoatRole.Coach, BoatRole.Cox));
        assertEquals(boatDTO.getCoxCertificateId(), cert.getId());
        assertNotNull(boatDTO.getBoatId());
    }

    @Test
    void getBoatTest() {
        var cert = new CertificateDTO(
                null,
                "be2s55t cert",
                null
        );

        cert = addCertificateToTheDatabase(cert);

        var boat = BoatDTO.builder()
                .name("good_boat1")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.Cox))
                .coxCertificateId(cert.getId())
                .build();
        var boatDTO = gatewayAdminClient.addBoat("Bearer " + adminToken, boat);
        var queriedBoat = gatewayBoatsClient.getBoat("Bearer " + userToken, boatDTO.getBoatId());
        assertEquals(queriedBoat.getName(), "good_boat1");
        assertEquals(queriedBoat.getAvailablePositions(), List.of(BoatRole.Coach, BoatRole.Cox));
        assertEquals(queriedBoat.getCoxCertificateId(), cert.getId());
        assertNotNull(queriedBoat.getBoatId());
    }

    @Test
    void getAllBoatsTest() {
        var cert = new CertificateDTO(
                null,
                "best 22cert",
                null
        );

        cert = addCertificateToTheDatabase(cert);

        var boat = BoatDTO.builder()
                .name("good_boat2")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.Cox))
                .coxCertificateId(cert.getId())
                .build();
        var boatDTO = gatewayAdminClient.addBoat("Bearer " + adminToken, boat);
        var queriedBoat = gatewayBoatsClient.getBoat("Bearer " + userToken,
                boatDTO.getBoatId());

        var allBoats = gatewayBoatsClient.getAllBoats("Bearer " + userToken);

        assertTrue(allBoats.contains(queriedBoat));
    }

    @Test
    void renameBoatTest() {
        var cert = new CertificateDTO(
                null,
                "best65 cert",
                null
        );

        cert = addCertificateToTheDatabase(cert);

        var boat = BoatDTO.builder()
                .name("good_boat4")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.Cox))
                .coxCertificateId(cert.getId())
                .build();
        var boatDTO = gatewayAdminClient.addBoat("Bearer " + adminToken, boat);
        gatewayAdminClient.renameBoat("Bearer " + adminToken, boatDTO.getBoatId(), "changed_boat4");
        var queriedBoat = gatewayBoatsClient.getBoat("Bearer " + userToken, boatDTO.getBoatId());

        assertEquals(queriedBoat.getName(), "changed_boat4");
        assertEquals(queriedBoat.getAvailablePositions(), List.of(BoatRole.Coach, BoatRole.Cox));
        assertEquals(queriedBoat.getCoxCertificateId(), cert.getId());
        assertNotNull(queriedBoat.getBoatId());
    }

    @Test
    void deleteBoatTest() {
        var cert = new CertificateDTO(
                null,
                "be34324st cert",
                null
        );

        cert = addCertificateToTheDatabase(cert);

        var boat = BoatDTO.builder()
                .name("good_boat7")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.Cox))
                .coxCertificateId(cert.getId())
                .build();
        var boatDTO = gatewayAdminClient.addBoat("Bearer " + adminToken, boat);
        assertDoesNotThrow(() -> gatewayBoatsClient.getBoat("Bearer " + userToken, boatDTO.getBoatId()));
        gatewayAdminClient.deleteBoat("Bearer " + adminToken, boatDTO.getBoatId());
        assertThrows(Exception.class, () -> gatewayBoatsClient.getBoat("Bearer " + userToken, boatDTO.getBoatId()));
    }

    @Test
    void addPositionTest() {
        var cert = new CertificateDTO(
                null,
                "54bes3456t cert",
                null
        );

        cert = addCertificateToTheDatabase(cert);

        var boat = BoatDTO.builder()
                .name("good_boat11")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.Cox))
                .coxCertificateId(cert.getId())
                .build();
        var boatDTO = gatewayAdminClient.addBoat("Bearer " + adminToken, boat);
        gatewayAdminClient.addPositionToBoat("Bearer " + adminToken, boatDTO.getBoatId(), BoatRole.ScullingRower);
        var queriedBoat = gatewayBoatsClient.getBoat("Bearer " + userToken, boatDTO.getBoatId());

        assertEquals(queriedBoat.getAvailablePositions(), List.of(BoatRole.Coach, BoatRole.Cox, BoatRole.ScullingRower));
        assertEquals(queriedBoat.getCoxCertificateId(), cert.getId());
        assertNotNull(queriedBoat.getBoatId());
    }

    @Test
    void removePositionTest() {
        var cert = new CertificateDTO(
                null,
                "best cer55t",
                null
        );

        cert = addCertificateToTheDatabase(cert);

        var boat = BoatDTO.builder()
                .name("good_boat155")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.Cox))
                .coxCertificateId(cert.getId())
                .build();
        var boatDTO = gatewayAdminClient.addBoat("Bearer " + adminToken, boat);
        gatewayAdminClient.removePositionFromBoat("Bearer " + adminToken, boatDTO.getBoatId(), BoatRole.Coach);
        var queriedBoat = gatewayBoatsClient.getBoat("Bearer " + userToken, boatDTO.getBoatId());

        assertEquals(queriedBoat.getAvailablePositions(), List.of(BoatRole.Cox));
        assertEquals(queriedBoat.getCoxCertificateId(), cert.getId());
        assertNotNull(queriedBoat.getBoatId());
    }

    @Test
    void changeCertificateBoatTest() {
        var cert = new CertificateDTO(
                null,
                "b5est cert",
                null
        );

        cert = addCertificateToTheDatabase(cert);

        var otherCert = new CertificateDTO(
                null,
                "be32st cert2",
                cert.getId()
        );

        otherCert = addCertificateToTheDatabase(otherCert);

        var boat = BoatDTO.builder()
                .name("good_boat13")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.Cox))
                .coxCertificateId(cert.getId())
                .build();
        var boatDTO = gatewayAdminClient.addBoat("Bearer " + adminToken, boat);
        gatewayAdminClient.changeCoxCertificate("Bearer " + adminToken, boatDTO.getBoatId(), otherCert.getId());
        var queriedBoat = gatewayBoatsClient.getBoat("Bearer " + userToken, boatDTO.getBoatId());

        assertEquals(queriedBoat.getCoxCertificateId(), otherCert.getId());
        assertNotNull(queriedBoat.getBoatId());
    }
}
