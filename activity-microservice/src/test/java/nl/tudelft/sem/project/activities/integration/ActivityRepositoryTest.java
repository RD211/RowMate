package nl.tudelft.sem.project.activities.integration;

import nl.tudelft.sem.project.activities.database.entities.Activity;
import nl.tudelft.sem.project.activities.database.entities.Boat;
import nl.tudelft.sem.project.activities.database.entities.Training;
import nl.tudelft.sem.project.activities.database.repository.ActivityRepository;
import nl.tudelft.sem.project.enums.BoatRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles({"test"})
public class ActivityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ActivityRepository repo;

    protected UUID setUUID =  UUID.fromString("0000-00-00-00-000000");

    @BeforeEach
    void setUp() {
        Boat boat = new Boat();
        boat.setName("test boat");
        boat.setAvailablePositions(List.of(BoatRole.Coach));
        boat.setCoxCertificateId(UUID.randomUUID());
        boat = entityManager.persist(boat);

        Activity activity = Training.builder()
                .location("test")
                .owner("userOne")
                .startTime(LocalDateTime.of(2022, 12, 18, 10, 0))
                .endTime(LocalDateTime.of(2022, 12, 18, 10, 50))
                .boats(List.of(boat))
                .build();

        entityManager.persist(activity);
    }

    @Test
    void testFindWithinTimeSlot() {
        assertThat(repo.findWithinTimeSlot(
                LocalDateTime.of(2022, 12, 15, 10, 0),
                LocalDateTime.of(2022, 12, 15, 12, 0)
        )).isEmpty();

        assertThat(repo.findWithinTimeSlot(
                LocalDateTime.of(2022, 12, 18, 9, 30),
                LocalDateTime.of(2022, 12, 18, 10, 50)
        )).isNotEmpty();
    }
}