package nl.tudelft.sem.project.authentication.domain.user;

import lombok.NonNull;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.shared.UsernameInUseException;
import org.springframework.stereotype.Service;

/**
 * A DDD service for registering a new user.
 */
@Service
public class RegistrationService {

    transient UserRepository userRepository;
    transient PasswordHashingService passwordHashingService;

    public RegistrationService(UserRepository userRepository, PasswordHashingService passwordHashingService) {
        this.userRepository = userRepository;
        this.passwordHashingService = passwordHashingService;
    }

    public AppUser registerUser(@NonNull Username username, @NonNull Password password) throws Exception {

        if (checkUsernameIsUnique(username)) {
            // Hash password
            HashedPassword hashedPassword = passwordHashingService.hash(password);

            // Create new account
            AppUser user = new AppUser(username, hashedPassword);
            userRepository.save(user);

            return user;
        }

        throw new UsernameInUseException("Username is in use. Try a different one.");
    }

    public boolean checkUsernameIsUnique(Username username) {
        return !userRepository.existsByUsername(username);
    }
}
