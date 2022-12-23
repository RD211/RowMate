package nl.tudelft.sem.project.gateway;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Generated
public class AuthenticateUserModel {
    @NotNull(message = "Username must not be null.")
    @Size(min = 4, max = 50, message = "Name must be between 4 and 50 characters.")
    String username;
    @NotNull(message = "Username must not be null.")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    String password;
}
