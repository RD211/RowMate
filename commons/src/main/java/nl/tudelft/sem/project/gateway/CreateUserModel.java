package nl.tudelft.sem.project.gateway;

import lombok.*;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Generated
public class CreateUserModel {
    @NotNull(message = "Username must not be null.")
    @Size(min = 4, max = 50, message = "Name must be between 4 and 50 characters.")
    String username;
    @NotNull(message = "Email must not be null.")
    @Email(message = "Email must be valid.")
    String email;
    @NotNull(message = "Username must not be null.")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    String password;
}
