package nl.tudelft.sem.project.users.database.entities;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class UserEmail {
    private final transient String email;

    public UserEmail(String email) {
        // Validate input
        this.email = email;
    }

    @Override
    public String toString() {
        return email;
    }
}