package nl.tudelft.sem.template.example.authentication;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Request filter for JWT security.
 * <p>
 * The request filter is called once for each request and makes it possible to modify the request
 * before it reaches the application. If an authorization header is present in the request,
 * the filter will validate it and authenticate the token.
 * </p>
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String WWW_AUTHENTICATE_HEADER = "WWW-Authenticate";
    public static final String AUTHORIZATION_AUTH_SCHEME = "Bearer";

    private final transient JwtTokenVerifier jwtTokenVerifier;

    @Autowired
    public JwtRequestFilter(JwtTokenVerifier jwtTokenVerifier) {
        this.jwtTokenVerifier = jwtTokenVerifier;
    }

    /**
     * This filter will verify and authenticate a JWT token if a valid authorization header is set.
     *
     * @param request     The current request we are handling.
     * @param response    The current response we are building.
     * @param filterChain The next link in the filter chain.
     * @throws ServletException Exception.
     * @throws IOException      Exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Get authorization header
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        // Check if an authorization header is set
        if (authorizationHeader != null) {
            String[] directives = authorizationHeader.split(" ");

            // Check for the correct auth scheme
            if (directives.length == 2 && directives[0].equals(AUTHORIZATION_AUTH_SCHEME)) {
                String token = directives[1];

                try {
                    if (jwtTokenVerifier.validateToken(token)) {
                        String netId = jwtTokenVerifier.getNetIdFromToken(token);
                        var authenticationToken = new UsernamePasswordAuthenticationToken(
                                netId,
                                null, List.of() // no credentials and no authorities
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                        // After setting the Authentication in the context, we specify
                        // that the current user is authenticated. So it passes the
                        // Spring Security Configurations successfully.
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }

                } catch (ExpiredJwtException e) {
                    System.err.println("JWT token has expired.");
                } catch (IllegalArgumentException | JwtException e) {
                    System.err.println("Unable to parse JWT token");
                }
            }
            System.err.println("Invalid authorization header");
        }

        filterChain.doFilter(request, response);
    }
}