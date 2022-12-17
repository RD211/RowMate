package nl.tudelft.sem.project.authentication.domain.user;

import lombok.*;
import nl.tudelft.sem.project.shared.Username;
import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AppUser {

    @Column(nullable = false, unique = true)
    @EmbeddedId
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
