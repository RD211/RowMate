package nl.tudelft.sem.project.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.project.shared.Username;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Model representing an authentication request.
 */
@Data
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserModel {
    @Valid @NotNull
    private Username username;
    @Valid @NotNull
    private Password password;
}