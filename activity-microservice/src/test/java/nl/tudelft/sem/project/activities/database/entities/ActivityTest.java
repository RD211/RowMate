package nl.tudelft.sem.project.activities.database.entities;

import nl.tudelft.sem.project.enums.BoatRole;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivityTest {

    @Test
    public void testActivityToDTO() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        List<UUID> boats = new ArrayList<>();
        Boat boat1 = Boat.builder()
                .id(id1)
                .name("Test Name")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.Cox))
                .build();

        Boat boat2 = Boat.builder()
                .id(id2)
                .name("Test Name")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.Cox))
                .build();
        boats.add(boat1.getId());
        boats.add(boat2.getId());

        Activity activity = Activity.builder()
                .id(id3)
                .location("Delft")
                .startTime(LocalDateTime.MIN)
                .endTime(LocalDateTime.MAX)
                .boats(boats)
                .build();

        var dto = activity.toDTO();

        assertThat(dto.getStartTime().isEqual(LocalDateTime.MIN));
        assertThat(dto.getId()).isEqualTo(id3);
        assertThat(dto.getLocation()).isEqualTo("Delft");
        assertThat(dto.getBoats()).isEqualTo(boats);
    }
}
