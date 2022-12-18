package nl.tudelft.sem.project.matchmaking.integration;


import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistration;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ActivityRegistrationRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ActivityRegistrationRepository repo;

    protected UUID setUUID =  UUID.fromString("0000-00-00-00-000000");

    @BeforeEach
    void setUp() {
        entityManager.persist(new ActivityRegistration("userOne", setUUID, 1, BoatRole.Coach));
    }

    @Test
    void testFindOverlap() {
        assertThat(repo.findRequestOverlap(setUUID, "userOne", 0, BoatRole.Cox)).isNotEmpty();
        assertThat(repo.findRequestOverlap(setUUID, "userTwo", 1, BoatRole.Coach)).isNotEmpty();

        assertThat(repo.findRequestOverlap(setUUID, "userTwo", 0, BoatRole.Cox)).isEmpty();
    }


}
