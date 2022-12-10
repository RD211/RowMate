package nl.tudelft.sem.project.users.domain.users;

import lombok.*;
import nl.tudelft.sem.project.entities.DTOable;
import nl.tudelft.sem.project.entities.shared.Organization;
import nl.tudelft.sem.project.entities.shared.OrganizationAttributeConverter;
import nl.tudelft.sem.project.entities.users.UserDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.users.domain.certificate.Certificate;
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
    @Convert(converter = UsernameAttributeConverter.class)
    protected Username username;

    @Column(nullable = false, unique = true)
    @Convert(converter = UserEmailAttributeConverter.class)
    protected UserEmail email;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    protected Gender gender;

    @Column(nullable = true)
    @Convert(converter = OrganizationAttributeConverter.class)
    protected Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @ElementCollection
    protected List<BoatRole> boatRoles;

    @ElementCollection
    protected List<DateInterval> availableTime;

    @OneToMany
    @EqualsAndHashCode.Exclude
    protected List<Certificate> certificates;

    @Override
    public UserDTO toDTO() {
        return new UserDTO(
                this.id,
                this.username.toString(),
                this.email.toString()
        );
    }

    /**
     * The user constructor given the dto.
     *
     * @param dto the user dto.
     */
    public User(UserDTO dto) {
        this.id = dto.getId();
        this.username = new Username(dto.getUsername());
        this.email = new UserEmail(dto.getEmail());
        this.boatRoles = new ArrayList<>();
        availableTime = new ArrayList<>();
    }
}
