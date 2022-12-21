package nl.tudelft.sem.project.authentication.domain.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.authentication.authentication.JwtTokenGenerator;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ChangePasswordServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordHashingService passwordHashingService;

    @InjectMocks
    ChangePasswordService changePasswordService;

    private final String secret = "testSecret123";

    /**
     * Set up secret.
     */
    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        this.injectSecret(secret);
    }

    @Test
    void changePasswordValid() {
        var user = new AppUser(new Username("tester"), new HashedPassword("test"), false);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        var newPassword = new Password("other new password");
        var hashedNewPassword = new HashedPassword("other new password hashed");
        when(passwordHashingService.hash(newPassword)).thenReturn(hashedNewPassword);
        assertDoesNotThrow(() -> changePasswordService.changePassword(user.getUsername(), newPassword));
        verify(userRepository, times(1))
                .updatePasswordByUsername(hashedNewPassword, user.getUsername());
    }

    @Test
    void changePasswordUserNotFound() {
        var user = new AppUser(new Username("tester"), new HashedPassword("test"), false);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        var newPassword = new Password("other new password");
        var hashedNewPassword = new HashedPassword("other new password hashed");
        when(passwordHashingService.hash(newPassword)).thenReturn(hashedNewPassword);
        assertThrows(UserNotFoundException.class,
                () -> changePasswordService.changePassword(user.getUsername(), newPassword));
        verify(userRepository, times(0))
                .updatePasswordByUsername(hashedNewPassword, user.getUsername());
    }

    @Test
    void getUsernameFromTokenValid() {
        var token = Jwts.builder().setSubject("tester")
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        var result = changePasswordService.getUsernameFromToken(token);
        assertEquals(new Username("tester"), result);

    }

    private void injectSecret(String secret) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = changePasswordService.getClass().getDeclaredField("jwtSecret");
        declaredField.setAccessible(true);
        declaredField.set(changePasswordService, secret);
    }
}