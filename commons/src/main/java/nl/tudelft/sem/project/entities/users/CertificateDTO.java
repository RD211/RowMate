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
    private UUID id;
    private String name;

    /**
     * This field should specify which certificate (by id) is superseded by this one, if any.
     */
    private Optional<UUID> supersededId;

    private UUID forBoat;
}
