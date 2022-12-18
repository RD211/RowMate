package nl.tudelft.sem.project.authentication.domain.user;

import lombok.NonNull;
import nl.tudelft.sem.project.authentication.Password;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * A DDD service for hashing passwords.
 */
public class PasswordHashingService {

    private final transient PasswordEncoder encoder;

    public PasswordHashingService(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public HashedPassword hash(@NonNull Password password) {
        return new HashedPassword(encoder.encode(password.getPasswordValue()));
    }
}
