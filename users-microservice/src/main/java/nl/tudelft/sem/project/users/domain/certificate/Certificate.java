package nl.tudelft.sem.project.users.domain.certificate;

import lombok.*;
import nl.tudelft.sem.project.entities.users.CertificateDTO;
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
    private UUID id;

    @Column(name = "name", nullable = false)
    @Setter
    @Getter
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supersedes")
    @Getter
    @NonNull
    private Optional<Certificate> superseded;

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
        this.superseded = Optional.of(superseded);
    }

    /**
     * Creates a certificate object.
     * The boatUUIDReference should be a valid UUID from the boats service.
     *
     * @param certificateName The certificate name
     * @param superseded The certificate that this certificate supersedes
     * @param boatUUIDReference A UUID reference to a boat from the boats service
     */
    public Certificate(String certificateName, Certificate superseded, UUID boatUUIDReference) {
        this.id = UUID.randomUUID();
        this.name = certificateName;
        this.superseded = Optional.of(superseded);
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
        this.superseded = Optional.empty();
        this.forBoat = boatUUIDReference;
    }

    /**
     * Computes all certificates implied by possession of this certificate.
     *
     * @return List of all implied certificates
     */
    public List<Certificate> getAllFromCertificateChain() {
        List<Certificate> result = new ArrayList<>();
        Optional<Certificate> finger = Optional.of(this);
        while (finger.isPresent()) {
            result.add(finger.get());
            finger = finger.get().superseded;
            // Stop if for some reason we have circular supersedence
            if (Optional.of(this).equals(finger)) {
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
                Optional.empty(),
                forBoat
        );
        if (superseded.isPresent()) {
            res.setSupersededId(
                    Optional.of(superseded.get().getId())
            );
        }
        return res;
    }

    /**
     * Creates a certificate entity from a dto.
     *
     * @param dto Data transfer object
     */
    public Certificate(CertificateDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        // TODO should find the superseded certificate if any
        this.superseded = Optional.empty();
        this.forBoat = dto.getForBoat();
    }
}
