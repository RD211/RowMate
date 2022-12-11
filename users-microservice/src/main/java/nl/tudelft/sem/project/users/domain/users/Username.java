package nl.tudelft.sem.project.users.domain.users;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@Data
public class Username {
    private final String name;

    public Username(String name) {
        // Validate input
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
