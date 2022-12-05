package nl.tudelft.sem.project.users.domain.certificate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

/**
 * A DDD entity representing a boat certificate in our domain.
 */
@Entity
@Table(name = "certificates")
@NoArgsConstructor
@Getter
public class Certificate {

    /**
     * Identifier for a certificate.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(targetEntity = Certificate.class)
    @Column(name = "supersedes")
    private Certificate supersedes;

    @Column(name = "for_boat")
    private UUID forBoat;

    /**
     * Creates a certificate object.
     * The boatUUIDReference should be a valid UUID from the boats service.
     *
     * @param certificateName The certificate name
     * @param supersedes The certificate that this certificate supersedes
     * @param boatUUIDReference A UUID reference to a boat from the boats service
     */
    public Certificate(String certificateName, Certificate supersedes, UUID boatUUIDReference) {
        this.name = certificateName;
        this.supersedes = supersedes;
        this.forBoat = boatUUIDReference;
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Certificate certificate = (Certificate) o;
        return id.equals(certificate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
