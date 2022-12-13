package nl.tudelft.sem.project.authentication.domain.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A DDD value object representing a hashed password in our domain.
 */
@EqualsAndHashCode
@Data
public class HashedPassword {
    @Valid
    @NotNull
    private final transient String hash;

    public HashedPassword(@NonNull String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return getHash();
    }
}
