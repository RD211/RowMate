package nl.tudelft.sem.project.shared;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Organization {
    @Size(min = 4, max = 200, message = "Organization name must be between 4 and 200 characters.")
    private final transient String name;
}
