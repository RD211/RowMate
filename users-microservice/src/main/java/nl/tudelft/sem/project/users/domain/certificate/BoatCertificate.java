package nl.tudelft.sem.project.users.domain.certificate;

import java.util.Objects;
import javax.persistence.*;

import lombok.NoArgsConstructor;

/**
 * A DDD entity representing a boat certificate in our domain.
 */
@Entity
@Table(name = "certificates")
@NoArgsConstructor
public class BoatCertificate {
    /**
     * Identifier for the application user.
     */
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

//    @Column(name = "boat")
//    private Boat forBoat;

    @OneToOne()
    @Column(name = "supersedes")
    private BoatCertificate supersedesCertificate;

//    /**
//     * Create new application user.
//     *
//     * @param netId The NetId for the new user
//     * @param password The password for the new user
//     */
//    public AppUser(NetId netId, HashedPassword password) {
//        this.netId = netId;
//        this.password = password;
//        this.recordThat(new UserWasCreatedEvent(netId));
//    }
//
//    public void changePassword(HashedPassword password) {
//        this.password = password;
//        this.recordThat(new PasswordWasChangedEvent(this));
//    }
//
//    public NetId getNetId() {
//        return netId;
//    }
//
//    public HashedPassword getPassword() {
//        return password;
//    }
//
//    /**
//     * Equality is only based on the identifier.
//     */
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        AppUser appUser = (AppUser) o;
//        return id == (appUser.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(netId);
//    }
}
