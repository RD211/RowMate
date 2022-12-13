package nl.tudelft.sem.project.activities.database.entities;

import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class BoatTest {

    @Test
    public void testBoatToDTO() {
        UUID id = UUID.randomUUID();
        Boat boat = Boat.builder()
                .id(id)
                .name("Test Name")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.Cox))
                .build();

        var dto = boat.toDTO();

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

        var boat = new Boat(dto);

        assertThat(boat.getId()).isEqualTo(id);
        assertThat(boat.getName()).isEqualTo("DTO Boat");
        assertThat(boat.getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.PortSideRower);
    }
}