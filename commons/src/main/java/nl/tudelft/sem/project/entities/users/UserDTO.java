package nl.tudelft.sem.project.entities.users;

import lombok.*;
import nl.tudelft.sem.project.entities.DTO;


import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements DTO {
    /**
     * The user's ID.
     */
    protected UUID id;

    /**
     * User's name.
     */
    @NonNull
    protected String username;

    /**
     * User's email address.
     */
    @NonNull
    protected String email;
}
