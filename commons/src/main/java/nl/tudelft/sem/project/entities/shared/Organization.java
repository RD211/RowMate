package nl.tudelft.sem.project.entities.shared;

public class Organization {
    private final transient String name;

    public Organization(String name) {
        // Validate input
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
