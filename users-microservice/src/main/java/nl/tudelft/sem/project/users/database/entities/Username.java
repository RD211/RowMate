package nl.tudelft.sem.project.users.database.entities;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
public class Username {
    private final transient String name;

    public Username(String name) {
        // Validate input
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
