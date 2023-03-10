package nl.tudelft.sem.project.activities.database.entities;

import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * A training is one of the two different types of activities.
 * It will have a 'T' in the discriminator column activity_type.
 */
@Entity
@SuperBuilder(toBuilder = true)
@DiscriminatorValue("T")
public class Training extends Activity {
    public Training() {
    }

    public Training(UUID id,
                    String location,
                    String owner,
                    LocalDateTime startTime,
                    LocalDateTime endTime,
                    List<Boat> boats) {
        super(id, location, owner, startTime, endTime, boats);
    }

}
