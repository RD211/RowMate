package nl.tudelft.sem.project.activities.database.entities;

import lombok.*;
import nl.tudelft.sem.project.entities.DTOable;
import nl.tudelft.sem.project.entities.activities.BoatDTO;
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
public class Boat implements DTOable<BoatDTO> {

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
     * Creates a boat object from a DTO.
     *
     * @param dto DTO to create from.
     */
    public Boat(BoatDTO dto) {
        this.id = dto.getBoatId();
        this.name = dto.getName();
        this.availablePositions = new ArrayList<>(dto.getAvailablePositions());
    }

    @Override
    public BoatDTO toDTO() {
        return new BoatDTO(
                this.id,
                this.name,
                new ArrayList<>(this.availablePositions));
    }
}
