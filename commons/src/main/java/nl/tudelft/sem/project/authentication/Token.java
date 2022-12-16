package nl.tudelft.sem.project.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Model representing an authentication response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Valid @NotNull
    private String token;

    @Override
    public String toString() {
        return token;
    }
}