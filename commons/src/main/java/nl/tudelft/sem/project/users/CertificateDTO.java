package nl.tudelft.sem.project.users;

import lombok.*;
import nl.tudelft.sem.project.DTO;

import javax.validation.constraints.NotNull;
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
    @NotNull
    private String name;

    /**
     * This field should specify which certificate (by id) is superseded by this one, if any.
     */
    private UUID supersededId;
}
