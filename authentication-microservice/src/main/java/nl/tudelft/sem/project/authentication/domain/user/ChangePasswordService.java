package nl.tudelft.sem.project.authentication.domain.user;

import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordService {
    transient UserRepository userRepository;
    transient PasswordHashingService passwordHashingService;

    @Value("${jwt.secret}")  // automatically loads jwt.secret from resources/application.properties
    private transient String jwtSecret;

    public ChangePasswordService(UserRepository userRepository, PasswordHashingService passwordHashingService) {
        this.userRepository = userRepository;
        this.passwordHashingService = passwordHashingService;
    }

    /**
     * Changes the user password.
     *
     * @param username the username of the user.
     * @param newPassword the new password.
     * @throws Exception if the user is not found.
     */
    public void changePassword(@NonNull Username username, @NonNull Password newPassword) throws Exception {
        var optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException("Could not find user to reset password for.");
        }

        HashedPassword hashedPassword = passwordHashingService.hash(newPassword);

        userRepository.updatePasswordByUsername(hashedPassword, username);
    }

    /**
     * Extracts the username from the token.
     *
     * @param token the token.
     * @return the username.
     */
    public Username getUsernameFromToken(@NonNull String token) {
        return new Username(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject());
    }
}
