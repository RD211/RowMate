package nl.tudelft.sem.project.activities.database.entities;

import nl.tudelft.sem.project.activities.database.repository.BoatRepository;
import nl.tudelft.sem.project.activities.exceptions.BoatNotFoundException;
import nl.tudelft.sem.project.activities.exceptions.RoleNotFoundException;
import nl.tudelft.sem.project.enums.BoatRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BoatServiceTest {

    @Mock
    private BoatRepository boatRepository;

    @InjectMocks
    private BoatService boatService;

    private Boat normalBoat;
    private Boat duplicateRoles;
    private UUID missingId;

    private UUID oldCertId;
    private UUID newCertId;

    @BeforeEach
    void setUp() {
        missingId = UUID.randomUUID();
        when(boatRepository.findById(missingId)).thenReturn(Optional.empty());
        normalBoat = Boat.builder()
                .id(UUID.randomUUID())
                .name("Normal Boat")
                .availablePositions(List.of(BoatRole.Cox, BoatRole.Coach))
                .coxCertificateId(oldCertId)
                .build();
        when(boatRepository.findById(normalBoat.getId())).thenReturn(Optional.ofNullable(normalBoat));
        when(boatRepository.save(normalBoat)).thenReturn(normalBoat);

        duplicateRoles = Boat.builder()
                .id(UUID.randomUUID())
                .name("Duplicates Boat")
                .availablePositions(List.of(BoatRole.ScullingRower, BoatRole.ScullingRower, BoatRole.Cox))
                .coxCertificateId(oldCertId)
                .build();
        when(boatRepository.findById(duplicateRoles.getId())).thenReturn(Optional.ofNullable(duplicateRoles));
        when(boatRepository.save(duplicateRoles)).thenReturn(duplicateRoles);


    }

    @Test
    void addBoat() {
        var ret = boatService.addBoat(normalBoat);
        verify(boatRepository, times(1)).save(normalBoat);
        assertThat(ret).isEqualTo(normalBoat);
    }

    @Test
    void getBoatByIdRegular() {
        boatService.addBoat(normalBoat);

        Boat ret = boatService.getBoatById(normalBoat.getId());
        verify(boatRepository, times(1)).findById(normalBoat.getId());
        assertThat(ret).isEqualTo(normalBoat);
    }

    @Test
    void getBoatByIdMissing() {
        assertThrows(BoatNotFoundException.class, () -> boatService.getBoatById(missingId));
    }

    @Test
    void deleteBoatByIdNormal() {
        Boat ret = boatService.addBoat(normalBoat);
        boatService.deleteBoatById(ret.getId());
        verify(boatRepository, times(1)).delete(normalBoat);
    }

    @Test
    void deleteBoatByIdMissing() {
        assertThrows(BoatNotFoundException.class, () -> boatService.deleteBoatById(missingId));
    }

    @Test
    void renameBoatNormal() {
        Boat ret1 = boatService.addBoat(normalBoat);
        Boat ret = boatService.renameBoat(ret1.getId(), "New Name");
        verify(boatRepository, times(2)).save(any());
        assertThat(ret.getName()).isEqualTo("New Name");
    }

    @Test
    void renameBoatMissing() {
        assertThrows(BoatNotFoundException.class, () -> boatService.renameBoat(missingId, "Some Name"));
        verify(boatRepository, never()).save(any());
    }

    @Test
    void removeAvailablePositionFromBoatNormal() {
        Boat ret1 = boatService.addBoat(normalBoat);
        Boat ret = boatService.removeAvailablePositionFromBoat(ret1.getId(), BoatRole.Coach);
        verify(boatRepository, times(2)).save(any());
        assertThat(ret.getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.Cox);
    }

    @Test
    void removeAvailablePositionFromBoatMissingId() {
        assertThrows(BoatNotFoundException.class, () ->
                boatService.removeAvailablePositionFromBoat(missingId, BoatRole.ScullingRower));
        verify(boatRepository, never()).save(any());
    }

    @Test
    void removeAvailablePositionFromBoatMissingRole() {
        Boat ret = boatService.addBoat(normalBoat);
        assertThrows(RoleNotFoundException.class, () ->
                boatService.removeAvailablePositionFromBoat(ret.getId(), BoatRole.ScullingRower));
        verify(boatRepository, times(1)).save(any());
    }

    @Test
    void removeAvailablePositionFromBoatDuplicatePositions() {
        Boat ret1 = boatService.addBoat(duplicateRoles);
        Boat ret = boatService.removeAvailablePositionFromBoat(ret1.getId(), BoatRole.ScullingRower);
        verify(boatRepository, times(2)).save(any());
        assertThat(ret.getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.Cox, BoatRole.ScullingRower);
    }

    @Test
    void addAvailablePositionToBoatNormal() {
        Boat ret1 = boatService.addBoat(normalBoat);
        Boat ret = boatService.addAvailablePositionToBoat(ret1.getId(), BoatRole.ScullingRower);
        verify(boatRepository, times(2)).save(any());
        assertThat(ret.getAvailablePositions())
                .containsExactlyInAnyOrder(BoatRole.Cox, BoatRole.Coach, BoatRole.ScullingRower);
    }

    @Test
    void addAvailablePositionToBoatMissing() {
        assertThrows(BoatNotFoundException.class, () ->
                boatService.addAvailablePositionToBoat(missingId, BoatRole.ScullingRower));
        verify(boatRepository, never()).save(any());
    }

    @Test
    void addAvailablePositionToBoatDuplicateRole() {
        Boat ret1 = boatService.addBoat(normalBoat);
        Boat ret = boatService.addAvailablePositionToBoat(ret1.getId(), BoatRole.Coach);
        verify(boatRepository, times(2)).save(any());
        assertThat(ret.getAvailablePositions())
                .containsExactlyInAnyOrder(BoatRole.Cox, BoatRole.Coach, BoatRole.Coach);
    }

    @Test
    void changeCoxCertificateNormal() {
        Boat ret1 = boatService.addBoat(normalBoat);
        Boat ret = boatService.changeCoxCertificate(ret1.getId(), newCertId);
        verify(boatRepository, times(2)).save(any());
        assertThat(ret.getCoxCertificateId()).isEqualTo(newCertId);
    }

    @Test
    void changeCoxCertificateMissing() {
        assertThrows(BoatNotFoundException.class, () ->
                boatService.changeCoxCertificate(missingId, newCertId));
        verify(boatRepository, never()).save(any());
    }

    @Test
    void changeCoxCertificateActuallyChangesIt() {
        var certificateId = UUID.randomUUID();
        Boat addedBoat = boatService.addBoat(normalBoat);
        boatService.changeCoxCertificate(addedBoat.getId(), certificateId);
        assertThat(addedBoat.getCoxCertificateId()).isEqualTo(certificateId);
    }

    //TODO add a test case for certificate verification when there is a certificate missing exception
}