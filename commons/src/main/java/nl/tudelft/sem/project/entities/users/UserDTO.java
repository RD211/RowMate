package nl.tudelft.sem.project.entities.users;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import nl.tudelft.sem.project.entities.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDTO implements DTO {
    /**
     * The user's ID.
     */
    protected UUID id;

    /**
     * User's name.
     */
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters long.")
    @NotNull(message = "Username must be set.")
    @NonNull
    protected String username;

    /**
     * User's email address.
     */
    @Email(message = "Invalid email address.")
    @Size(max = 50, message = "Email address must be less than 50 characters long.")
    @NotNull(message = "Email must be set.")
    @NonNull
    protected String email;
}
