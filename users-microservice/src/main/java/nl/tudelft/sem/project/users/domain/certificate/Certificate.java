package nl.tudelft.sem.project.users.domain.certificate;

import lombok.*;
import nl.tudelft.sem.project.entities.users.CertificateDTO;
import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import org.hibernate.annotations.GenericGenerator;
import nl.tudelft.sem.project.entities.DTOable;

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

    @Column(name = "for_boat")
    @Getter
    @Setter
    private UUID forBoat;

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
     * @param boatUUIDReference A UUID reference to a boat from the boats service
     */
    public Certificate(String certificateName, @NonNull Certificate superseded, UUID boatUUIDReference) {
        this.id = UUID.randomUUID();
        this.name = certificateName;
        this.superseded = superseded;
        this.forBoat = boatUUIDReference;
    }

    /**
     * Creates a certificate object.
     * The boatUUIDReference should be a valid UUID from the boats service.
     * The certificate will not supersede any other certificate.
     *
     * @param certificateName The certificate name
     * @param boatUUIDReference A UUID reference to a boat from the boats service
     */
    public Certificate(String certificateName, UUID boatUUIDReference) {
        this.id = UUID.randomUUID();
        this.name = certificateName;
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
            finger = finger.superseded;
            // Stop if for some reason we have circular supersedence
            if (this.equals(finger)) {
                break;
            }
        }
        return result;
    }

    @Override
    public CertificateDTO toDTO() {
        var res = new CertificateDTO(
                id,
                name,
                Optional.ofNullable(superseded).map(c -> c.getId()),
                forBoat
        );
        return res;
    }

    /**
     * Creates a certificate entity from a dto.
     *
     * @param dto Data transfer object
     */
    public Certificate(CertificateDTO dto, CertificateRepository repo) throws SupersededCertificateDoesNotExist {
        this.id = dto.getId();
        this.name = dto.getName();
        if (dto.getSupersededId().isPresent()) {
            UUID supersededId = dto.getSupersededId().get();
            Optional<Certificate> maybeSuperseded = repo.findById(supersededId);
            if (maybeSuperseded.isEmpty()) throw new SupersededCertificateDoesNotExist();
            this.superseded = maybeSuperseded.get();
        }
        this.forBoat = dto.getForBoat();
    }
}
