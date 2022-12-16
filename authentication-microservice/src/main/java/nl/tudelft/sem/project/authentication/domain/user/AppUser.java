package nl.tudelft.sem.project.authentication.domain.user;

import lombok.*;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.shared.UsernameAttributeConverter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;


@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AppUser {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    @Column(nullable = false, unique = true)
    @Convert(converter = UsernameAttributeConverter.class)
    protected Username username;

    @Column(nullable = false)
    @Convert(converter = HashedPasswordAttributeConverter.class)
    protected HashedPassword password;

    @Column(nullable = false)
    protected boolean isAdmin = false;

    public AppUser(Username username, HashedPassword password) {
        this.username = username;
        this.password = password;
    }
}
