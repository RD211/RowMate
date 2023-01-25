package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.BoatsClient;
import nl.tudelft.sem.project.enums.BoatRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BoatControllerTest {

    @Mock
    BoatsClient boatsClient;

    @InjectMocks
    BoatController boatController;

    @Test
    void getBoatById() {
        UUID id = UUID.randomUUID();
        BoatDTO boat = BoatDTO.builder()
                .boatId(id)
                .availablePositions(List.of(BoatRole.Cox))
                .build();
        when(boatsClient.getBoat(id)).thenReturn(boat);
        var ret = boatController.getBoatById(id);
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ret.getBody()).isEqualTo(boat);
        verify(boatsClient, times(1)).getBoat(id);
    }

    @Test
    void getAllBoats() {
        UUID id = UUID.randomUUID();
        BoatDTO boat = BoatDTO.builder()
                .boatId(id)
                .availablePositions(List.of(BoatRole.Cox))
                .build();
        when(boatsClient.getAllBoats()).thenReturn(List.of(boat));
        var ret = boatController.getAllBoats();
        assertThat(ret.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ret.getBody()).containsExactlyInAnyOrder(boat);
        verify(boatsClient, times(1)).getAllBoats();
    }
}