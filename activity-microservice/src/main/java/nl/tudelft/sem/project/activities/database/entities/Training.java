package nl.tudelft.sem.project.activities.database.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * A training is one of the two different types of activities.
 * It will have a 'T' in the discriminator column activity_type.
 */
@Entity
@DiscriminatorValue("T")
public class Training extends Activity {

}
