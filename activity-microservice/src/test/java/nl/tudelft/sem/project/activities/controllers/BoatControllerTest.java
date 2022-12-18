package nl.tudelft.sem.project.activities.controllers;

import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.database.entities.Boat;
import nl.tudelft.sem.project.activities.database.entities.BoatService;
import nl.tudelft.sem.project.activities.database.repository.BoatRepository;
import nl.tudelft.sem.project.activities.exceptions.BoatNotFoundException;
import nl.tudelft.sem.project.enums.BoatRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
class BoatControllerTest {

    @Mock
    BoatRepository boatRepository;

    @Mock
    BoatService boatService;

    @InjectMocks
    BoatController boatController;

    @Test
    void testAddBoat() {

        BoatDTO boatDTO = BoatDTO.builder()
                .boatId(UUID.randomUUID())
                .name("Test Boat")
                .availablePositions(List.of(BoatRole.Cox))
                .build();

        UUID retId = UUID.randomUUID();
        Boat retBoat = Boat.builder()
                .id(retId)
                .name("Test Boat")
                .availablePositions(List.of(BoatRole.Cox))
                .build();

        when(boatService.addBoat(argThat(x -> x.getName().equals("Test Boat")))).thenReturn(retBoat);

        var ret = boatController.addBoat(boatDTO);
        verify(boatService, times(1)).addBoat(argThat(x -> x.getName().equals("Test Boat")));
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ret.getBody().getName()).isEqualTo("Test Boat");
        assertThat(ret.getBody().getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.Cox);
        assertThat(ret.getBody().getBoatId()).isEqualTo(retId);
    }

    @Test
    void getBoatNormal() {
        UUID boatId = UUID.randomUUID();
        Boat boat = Boat.builder()
                .id(boatId)
                .name("Epic Boat")
                .availablePositions(List.of(BoatRole.PortSideRower))
                .build();

        when(boatService.getBoatById(boatId)).thenReturn(boat);
        var ret = boatController.getBoat(boatId);
        verify(boatService, times(1)).getBoatById(boatId);
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ret.getBody().getName()).isEqualTo("Epic Boat");
        assertThat(ret.getBody().getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.PortSideRower);
        assertThat(ret.getBody().getBoatId()).isEqualTo(boatId);
    }

    @Test
    void getBoatNotFound() {
        UUID boatId = UUID.randomUUID();
        Boat boat = Boat.builder()
                .id(boatId)
                .name("Epic Boat")
                .availablePositions(List.of(BoatRole.PortSideRower))
                .build();

        when(boatService.getBoatById(boatId)).thenThrow(BoatNotFoundException.class);

        assertThatThrownBy(() -> boatController.getBoat(boatId)).isInstanceOf(BoatNotFoundException.class);
        verify(boatService, times(1)).getBoatById(boatId);
    }

    @Test
    void renameBoatNormal() {
        UUID boatId = UUID.randomUUID();
        Boat newBoat = Boat.builder()
                .id(boatId)
                .name("New Name Boat")
                .availablePositions(List.of(BoatRole.PortSideRower))
                .build();

        when(boatService.renameBoat(boatId, "New Name Boat")).thenReturn(newBoat);
        var ret = boatController.renameBoat(boatId, "New Name Boat");
        verify(boatService, times(1)).renameBoat(boatId, "New Name Boat");
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ret.getBody().getName()).isEqualTo("New Name Boat");
        assertThat(ret.getBody().getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.PortSideRower);
        assertThat(ret.getBody().getBoatId()).isEqualTo(boatId);
    }

    @Test
    void renameBoatNotFound() {
        UUID boatId = UUID.randomUUID();
        Boat boat = Boat.builder()
                .id(boatId)
                .name("Le Boat")
                .availablePositions(List.of(BoatRole.PortSideRower))
                .build();

        when(boatService.renameBoat(boatId, "New Name")).thenThrow(BoatNotFoundException.class);
        assertThatThrownBy(() -> boatController.renameBoat(boatId, "New Name")).isInstanceOf(BoatNotFoundException.class);
        verify(boatService, times(1)).renameBoat(boatId, "New Name");

    }

    @Test
    void deleteBoatNormal() {
        UUID boatId = UUID.randomUUID();
        doNothing().when(boatService).deleteBoatById(boatId);
        var ret = boatController.deleteBoat(boatId);
        verify(boatService, times(1)).deleteBoatById(boatId);
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteBoatNotFound() {
        UUID boatId = UUID.randomUUID();
        doThrow(BoatNotFoundException.class).when(boatService).deleteBoatById(boatId);
        assertThatThrownBy(() -> boatController.deleteBoat(boatId)).isInstanceOf(BoatNotFoundException.class);
        verify(boatService, times(1)).deleteBoatById(boatId);
    }

    @Test
    void addPositionToBoat() {
        UUID boatId = UUID.randomUUID();
        Boat boat = Boat.builder()
                .id(boatId)
                .name("Added Boat")
                .availablePositions(List.of(BoatRole.Cox))
                .build();
        when(boatService.addAvailablePositionToBoat(boatId, BoatRole.Cox)).thenReturn(boat);
        var ret = boatController.addPositionToBoat(boatId, BoatRole.Cox);
        verify(boatService, times(1)).addAvailablePositionToBoat(boatId, BoatRole.Cox);
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ret.getBody().getBoatId()).isEqualTo(boatId);
        assertThat(ret.getBody().getName()).isEqualTo("Added Boat");
        assertThat(ret.getBody().getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.Cox);
    }

    @Test
    void removePositionFromBoat() {
        UUID boatId = UUID.randomUUID();
        Boat boat = Boat.builder()
                .id(boatId)
                .name("Remove Boat")
                .availablePositions(List.of(BoatRole.Cox))
                .build();
        when(boatService.removeAvailablePositionFromBoat(boatId, BoatRole.Coach)).thenReturn(boat);
        var ret = boatController.removePositionFromBoat(boatId, BoatRole.Coach);
        verify(boatService, times(1)).removeAvailablePositionFromBoat(boatId, BoatRole.Coach);
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ret.getBody().getBoatId()).isEqualTo(boatId);
        assertThat(ret.getBody().getName()).isEqualTo("Remove Boat");
        assertThat(ret.getBody().getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.Cox);
    }

    @Test
    void changeCoxCertificate() {
        UUID boatId = UUID.randomUUID();
        UUID newCertId = UUID.randomUUID();
        Boat boat = Boat.builder()
                .id(boatId)
                .name("Remove Boat")
                .availablePositions(List.of(BoatRole.Cox))
                .coxCertificateId(newCertId)
                .build();
        when(boatService.changeCoxCertificate(boatId, newCertId)).thenReturn(boat);
        var ret = boatController.changeCoxCertificate(boatId, newCertId);
        verify(boatService, times(1)).changeCoxCertificate(boatId, newCertId);
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ret.getBody().getBoatId()).isEqualTo(boatId);
        assertThat(ret.getBody().getName()).isEqualTo("Remove Boat");
        assertThat(ret.getBody().getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.Cox);
        assertThat(ret.getBody().getCoxCertificateId()).isEqualTo(newCertId);
    }
}