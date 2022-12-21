package nl.tudelft.sem.project.gateway.authentication;

import lombok.Generated;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Authentication Manager.
 */
@Component
@Generated
public class AuthManager {
    /**
     * Interfaces with spring security to get the name of the user in the current context.
     *
     * @return The name of the user.
     */
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
