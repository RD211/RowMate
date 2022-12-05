package nl.tudelft.sem.project.users.domain.certificate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DDD entity representing a boat certificate in our domain.
 */
@Entity
@Table(name = "certificates")
@NoArgsConstructor
public class Certificate {

    /**
     * Identifier for a certificate.
     */
    @Getter
    @Setter
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Getter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "supersedes")
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
     * Computes all certificates implied by possession of this certificate.
     * @return List of all implied certificates
     */
    public List<Certificate> getAllFromCertificateChain() {
        List<Certificate> result = new ArrayList<>();
        Certificate finger = this;
        while (finger != null) {
            result.add(finger);
            finger = finger.supersedes;
            // Stop if for some reason we have circular supersedence
            if (finger == this) break;
        }
        return result;
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

    /**
     * Hash code is only based on the identifier.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
