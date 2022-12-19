package nl.tudelft.sem.project.users;

import lombok.*;
import nl.tudelft.sem.project.DTO;
import nl.tudelft.sem.project.utils.Existing;
import nl.tudelft.sem.project.utils.Fictional;

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
    @NotNull(groups = {Existing.class})
    private UUID id;

    /**
     * Specifies the name of the certificate.
     */
    @NotNull(groups = {Fictional.class})
    private String name;

    /**
     * This field should specify which certificate (by id) is superseded by this one, if any.
     */
    private UUID supersededId;
}
