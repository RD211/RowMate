package nl.tudelft.sem.project.authentication.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import nl.tudelft.sem.project.authentication.domain.providers.TimeProvider;
import nl.tudelft.sem.project.authentication.Token;
import nl.tudelft.sem.project.shared.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class JwtTokenGeneratorTests {

    @Mock
    private transient TimeProvider timeProvider;
    @InjectMocks
    private JwtTokenGenerator jwtTokenGenerator;
    protected transient Instant mockedTime = Instant.parse("2021-12-31T13:25:34.00Z");

    private final String secret = "testSecret123";

    private final Username username = new Username("andy");
    private UserDetails user;

    /**
     * Set up mocks.
     */
    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        when(timeProvider.getCurrentTime()).thenReturn(mockedTime);

        jwtTokenGenerator = new JwtTokenGenerator(timeProvider);
        this.injectSecret(secret);

        user = new User(username.getName(), "someHash", new ArrayList<>());
    }

    @Test
    public void generatedTokenHasCorrectIssuanceDate() {
        // Act
        Token token = jwtTokenGenerator.generateToken(user);

        // Assert
        Claims claims = getClaims(token.getToken());
        assertThat(claims.getIssuedAt()).isEqualTo(mockedTime.toString());
    }

    @Test
    public void generatedTokenHasCorrectExpirationDate() {
        // Act
        Token token = jwtTokenGenerator.generateToken(user);

        // Assert
        Claims claims = getClaims(token.getToken());
        assertThat(claims.getExpiration()).isEqualTo(mockedTime.plus(1, ChronoUnit.DAYS).toString());
    }

    @Test
    public void generatedTokenHasCorrectUsername() {
        // Act
        Token token = jwtTokenGenerator.generateToken(user);

        // Assert
        Claims claims = getClaims(token.getToken());
        assertThat(claims.getSubject()).isEqualTo(username.getName());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setAllowedClockSkewSeconds(Integer.MAX_VALUE)
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private void injectSecret(String secret) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = jwtTokenGenerator.getClass().getDeclaredField("jwtSecret");
        declaredField.setAccessible(true);
        declaredField.set(jwtTokenGenerator, secret);
    }
}
