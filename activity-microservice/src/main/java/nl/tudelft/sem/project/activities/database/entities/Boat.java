package nl.tudelft.sem.project.activities.database.entities;

import lombok.*;
import nl.tudelft.sem.project.DTOable;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Boat {

    /**
     * Unique boat ID.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    /**
     * The name of the boat used for display.
     */
    @Column(nullable = false)
    protected String name;

    /**
     * A list of all available positions for this boat. There can be duplicates
     * if there's multiple of a role needed.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ElementCollection
    protected List<BoatRole> availablePositions;

    /**
     * Id of the certificate that is required for a cox to be able to operate
     * the boat.
     */
    @Column(nullable = false)
    public UUID coxCertificateId;
}
