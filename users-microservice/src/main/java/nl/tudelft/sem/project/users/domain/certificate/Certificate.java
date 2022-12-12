package nl.tudelft.sem.project.users.domain.certificate;

import lombok.*;
import nl.tudelft.sem.project.DTOable;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
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
@Setter()
@Table(name = "certificates")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Certificate implements DTOable<CertificateDTO> {

    /**
     * Identifier for a certificate.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    @Setter
    @Getter
    @NonNull
    private UUID id;

    @Column(name = "name", nullable = false)
    @Setter
    @Getter
    @NonNull
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supersedes")
    private Certificate superseded;


    /**
     * Sets superseded certificate.
     *
     * @param superseded A non-null certificate
     */
    public void setSuperseded(@NonNull Certificate superseded) {
        this.superseded = superseded;
    }

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
        this.name = certificateName;
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
        this.name = certificateName;
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
    public boolean hasInChain(Certificate other) {
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

    @Override
    public CertificateDTO toDTO() {
        var res = new CertificateDTO(
                id,
                name,
                Optional.ofNullable(superseded).map(c -> c.getId())
        );
        return res;
    }

    /**
     * Creates a certificate entity from a dto.
     *
     * @param dto Data transfer object
     */
    public Certificate(CertificateDTO dto, CertificateRepository repo) throws CertificateNotFoundException {
        this.id = dto.getId();
        this.name = dto.getName();
        if (dto.getSupersededId().isPresent()) {
            UUID supersededId = dto.getSupersededId().get();
            Optional<Certificate> maybeSuperseded = repo.findById(supersededId);
            if (maybeSuperseded.isEmpty()) {
                throw new CertificateNotFoundException(supersededId);
            }
            this.superseded = maybeSuperseded.get();
        }
    }
}
