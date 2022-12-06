package nl.tudelft.sem.project.users.domain.certificate;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A DDD entity representing a boat certificate in our domain.
 * Equality and hashcode based on id only.
 */
@Entity
@Data
@Table(name = "certificates")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Certificate {

    /**
     * Identifier for a certificate.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
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
        this.id = UUID.randomUUID();
        this.name = certificateName;
        this.supersedes = supersedes;
        this.forBoat = boatUUIDReference;
    }

    /**
     * Computes all certificates implied by possession of this certificate.
     *
     * @return List of all implied certificates
     */
    public List<Certificate> getAllFromCertificateChain() {
        List<Certificate> result = new ArrayList<>();
        Certificate finger = this;
        while (finger != null) {
            result.add(finger);
            finger = finger.supersedes;
            // Stop if for some reason we have circular supersedence
            if (this.equals(finger)) {
                break;
            }
        }
        return result;
    }

}
