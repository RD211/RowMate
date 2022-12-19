package nl.tudelft.sem.project.users.domain.certificate;

import lombok.*;
import nl.tudelft.sem.project.users.CertificateName;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A DDD entity representing a boat certificate in our domain.
 * Equality and hashcode based on id only.
 */
@Entity
@Setter
@Table(name = "certificates")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
public class Certificate {

    /**
     * Identifier for a certificate.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    @Getter
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    @Getter
    @NonNull
    @Convert(converter = CertificateNameAttributeConverter.class)
    private CertificateName name;


    @ManyToOne
    @JoinColumn(name = "supersedes")
    @ToString.Exclude
    private Certificate superseded;

    /**
     * Gets the certificate superseded by this one.
     *
     * @return The superseded certificate. If there is none, then empty.
     */
    public Optional<Certificate> getSuperseded() {
        return Optional.ofNullable(superseded);
    }

    /**
     * Creates a certificate object.
     * The boatUUIDReference should be a valid UUID from the boats service.
     *
     * @param certificateName The certificate name
     * @param superseded The certificate that this certificate supersedes
     */
    public Certificate(String certificateName, @NonNull Certificate superseded) {
        this.id = UUID.randomUUID();
        this.name = new CertificateName(certificateName);
        this.superseded = superseded;
    }

    /**
     * Creates a certificate object.
     * The boatUUIDReference should be a valid UUID from the boats service.
     * The certificate will not supersede any other certificate.
     *
     * @param certificateName The certificate name
     */
    public Certificate(String certificateName) {
        this.id = UUID.randomUUID();
        this.name = new CertificateName(certificateName);
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
            finger = finger.superseded;
            // Stop if for some reason we have circular supersedence
            if (this.equals(finger)) {
                break;
            }
        }
        return result;
    }

    /**
     * Check whether other is contained in the supersedence chain of this.
     *
     * @param other Certificate to be looked for
     * @return True if other could be found in chain of this
     */
    public boolean hasInChain(final Certificate other) {
        Certificate finger = this;
        while (finger != null) {
            if (other.equals(finger)) {
                return true;
            }
            finger = finger.superseded;
            // Stop if for some reason we have circular supersedence
            if (this.equals(finger)) {
                break;
            }
        }
        return false;
    }

}
