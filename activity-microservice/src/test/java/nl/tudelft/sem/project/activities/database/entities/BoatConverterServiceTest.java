package nl.tudelft.sem.project.activities.database.entities;

import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BoatConverterServiceTest {

    @Mock
    BoatService boatService;

    @InjectMocks
    BoatConverterService boatConverterService;

    @Test
    public void testBoatToDTO() {
        UUID id = UUID.randomUUID();
        Boat boat = Boat.builder()
                .id(id)
                .name("Test Name")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.Cox))
                .build();

        var dto = boatConverterService.toDTO(boat);

        assertThat(dto.getBoatId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo("Test Name");
        assertThat(dto.getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.Coach, BoatRole.Cox);
    }

    @Test
    public void testDTOToBoat() {
        UUID id = UUID.randomUUID();
        BoatDTO dto = BoatDTO.builder()
                .boatId(id)
                .name("DTO Boat")
                .availablePositions(List.of(BoatRole.PortSideRower))
                .build();

        var boat = boatConverterService.toEntity(dto);

        assertThat(boat.getId()).isEqualTo(id);
        assertThat(boat.getName()).isEqualTo("DTO Boat");
        assertThat(boat.getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.PortSideRower);
    }

    @Test
    public void testDTOToDatabaseEntity() {
        UUID id = UUID.randomUUID();
        BoatDTO dto = BoatDTO.builder()
                .boatId(id)
                .name("DTO Boat")
                .availablePositions(List.of(BoatRole.PortSideRower))
                .build();

        when(boatService.getBoatById(dto.getBoatId())).thenReturn(
                boatConverterService.toEntity(dto)
        );
        var boat = boatConverterService.toDatabaseEntity(dto);

        assertThat(boat.getId()).isEqualTo(id);
        assertThat(boat.getName()).isEqualTo("DTO Boat");
        assertThat(boat.getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.PortSideRower);
    }
}