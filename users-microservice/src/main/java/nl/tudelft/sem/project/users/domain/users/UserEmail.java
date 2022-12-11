package nl.tudelft.sem.project.users.domain.users;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class UserEmail {
    private final String email;

    public UserEmail(String email) {
        // Validate input
        this.email = email;
    }

    @Override
    public String toString() {
        return email;
    }
}