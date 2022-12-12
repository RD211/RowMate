package nl.tudelft.sem.project.entities.users;

import lombok.*;
import nl.tudelft.sem.project.entities.DTO;

import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateDTO implements DTO {
    /**
     * The identifier of a certificate.
     */
    private UUID id;

    /**
     * Specifies the name of the certificate.
     */
    private String name;

    /**
     * This field should specify which certificate (by id) is superseded by this one, if any.
     */
    private Optional<UUID> supersededId;

}
