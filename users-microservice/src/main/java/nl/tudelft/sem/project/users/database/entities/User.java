package nl.tudelft.sem.project.users.database.entities;

import lombok.*;
import nl.tudelft.sem.project.entities.DTOable;
import nl.tudelft.sem.project.entities.users.UserDTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * The user entity.
 * Stores the details that are required for a user.
 * Extends DTO-able since it will need to be able to be transformed into
 * a dto.
 */
@Getter
@Setter
@ToString
@Entity
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class User implements DTOable<UserDTO> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    @Column(nullable = false, unique = true)
    protected String username;

    @Column(nullable = false, unique = true)
    protected String email;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    protected Gender gender;

    @Column(nullable = true)
    protected String organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @ElementCollection
    protected List<BoatRole> boatRoles;

    @ElementCollection
    protected List<DateInterval> availableTime;

    @Override
    public UserDTO toDTO() {
        return new UserDTO(
                this.id,
                this.username,
                this.email
        );
    }

    /**
     * The user constructor given the dto.
     *
     * @param dto the user dto.
     */
    public User(UserDTO dto) {
        this.id = dto.getId();
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.boatRoles = new ArrayList<>();
        availableTime = new ArrayList<>();
    }
}
