package nl.tudelft.sem.project.authentication.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import nl.tudelft.sem.project.authentication.domain.providers.TimeProvider;
import nl.tudelft.sem.project.authentication.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Generator for JWT tokens.
 */
@Component
public class JwtTokenGenerator {
    /**
     * Time in milliseconds the JWT token is valid for.
     */
    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;

    @Value("${jwt.secret}")  // automatically loads jwt.secret from resources/application.properties
    private transient String jwtSecret;

    /**
     * Time provider to make testing easier.
     */
    private final transient TimeProvider timeProvider;

    @Autowired
    public JwtTokenGenerator(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }


    public Token generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return new Token(Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(timeProvider.getCurrentTime().toEpochMilli()))
                .setExpiration(new Date(timeProvider.getCurrentTime().toEpochMilli() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact());
    }
}
