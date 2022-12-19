package nl.tudelft.sem.project.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
    @Size(min = 4, max = 200, message = "Organization name must be between 4 and 200 characters.")
    private String name;
}
